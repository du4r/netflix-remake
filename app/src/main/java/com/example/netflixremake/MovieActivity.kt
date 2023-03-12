package com.example.netflixremake

import android.graphics.drawable.LayerDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.netflixremake.models.Movie


class MovieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val movieTitle: TextView = findViewById(R.id.movie_txt_title)
        val movieDesc: TextView = findViewById(R.id.movie_text_desc)
        val movieCast: TextView = findViewById(R.id.movie_text_cast)
        val rvSimilar: RecyclerView = findViewById(R.id.movie_rv_similar)

        movieTitle.text = "Batman Begins"
        movieDesc.text = "descricao descrita"
        movieCast.text = "ator A ator B"

        val movies = mutableListOf<Movie>()



        rvSimilar.layoutManager = GridLayoutManager(this, 3)
        rvSimilar.adapter = MovieAdapter(movies, R.layout.movie_item_similar)

        val toolbar: Toolbar = findViewById(R.id.movie_toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        val layerDrawable: LayerDrawable = ContextCompat.getDrawable(this, R.drawable.shadows) as LayerDrawable

        val movieCover = ContextCompat.getDrawable(this, R.drawable.movie)
        layerDrawable.setDrawableByLayerId(R.id.cover_drawable, movieCover)
/*
        val coverImg: ImageView = findViewById(R.id.movie_img)
        coverImg.setImageDrawable(layerDrawable)*/

    }
}