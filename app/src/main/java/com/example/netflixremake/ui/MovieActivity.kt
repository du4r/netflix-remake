package com.example.netflixremake.ui

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LayerDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.netflixremake.ui.adapters.MovieAdapter
import com.example.netflixremake.R
import com.example.netflixremake.models.Movie
import com.example.netflixremake.models.MovieDetail
import com.example.netflixremake.utils.DownloadImageTask
import com.example.netflixremake.utils.MovieTask


class MovieActivity : AppCompatActivity(), MovieTask.Callback {

    private lateinit var movieTitle: TextView
    private lateinit var movieDesc: TextView
    private lateinit var movieCast: TextView
    private lateinit var rvSimilar: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: MovieAdapter
    private val movies = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        movieTitle = findViewById(R.id.movie_txt_title)
        movieDesc = findViewById(R.id.movie_text_desc)
        movieCast = findViewById(R.id.movie_text_cast)
        rvSimilar = findViewById(R.id.movie_rv_similar)
        progressBar = findViewById(R.id.movie_progress)


        val id = intent?.getIntExtra("id",0) ?: throw IllegalStateException("ID nao encontrado!")

        val url = "https://api.tiagoaguiar.co/netflixapp/movie/$id?apiKey=c1e9ecc5-c528-4a22-87ed-43948cf3062d"

        MovieTask(this).execute(url)

        adapter = MovieAdapter(movies, R.layout.movie_item_similar)
        rvSimilar.layoutManager = GridLayoutManager(this, 3)
        rvSimilar.adapter = adapter

        val toolbar: Toolbar = findViewById(R.id.movie_toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

    }

    override fun onPreExecute() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onResult(movieDetail: MovieDetail) {
        progressBar.visibility = View.GONE

        movieTitle.text = movieDetail.movie.title
        movieDesc.text = movieDetail.movie.desc
        movieCast.text = getString(R.string.cast, movieDetail.movie.cast)

        movies.clear()
        movies.addAll(movieDetail.similars)
        adapter.notifyDataSetChanged()


        DownloadImageTask(object : DownloadImageTask.Callback{
            override fun onResult(bitmap: Bitmap) {
                val layerDrawable: LayerDrawable = ContextCompat.getDrawable(this@MovieActivity,
                    R.drawable.shadows
                ) as LayerDrawable
                val movieCover = BitmapDrawable(resources,bitmap)
                layerDrawable.setDrawableByLayerId(R.id.cover_drawable, movieCover)
                val coverImg : ImageView = findViewById(R.id.movie_img)
                coverImg.setImageDrawable(layerDrawable)
            }
        }).execute(movieDetail.movie.coverUrl)

    }

    override fun onFailure(message: String) {
        progressBar.visibility = View.GONE
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}