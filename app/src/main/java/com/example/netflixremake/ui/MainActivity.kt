package com.example.netflixremake.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.netflixremake.ui.adapters.CategoryAdapter
import com.example.netflixremake.R
import com.example.netflixremake.models.Category
import com.example.netflixremake.utils.CategoryTask

class MainActivity : AppCompatActivity(), CategoryTask.Callback {

    private lateinit var rvMain: RecyclerView
    private lateinit var progressMain: ProgressBar
    private val categories = mutableListOf<Category>()
    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressMain = findViewById(R.id.progress_main)

        adapter = CategoryAdapter(categories){ id ->
            val intent = Intent(this@MainActivity, MovieActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }

        rvMain = findViewById(R.id.rv_main)
        rvMain.layoutManager = LinearLayoutManager(this)
        rvMain.adapter = adapter

        CategoryTask(this).execute("https://api.tiagoaguiar.co/netflixapp/home?apiKey=c1e9ecc5-c528-4a22-87ed-43948cf3062d")
    }

    override fun onPreExecute() {
        progressMain.visibility = View.VISIBLE
    }

    override fun onResult(categories: List<Category>) {
        Log.i("teste MainAct", categories.toString())
        this.categories.clear()
        this.categories.addAll(categories)
        adapter.notifyDataSetChanged()
        progressMain.visibility = View.GONE
    }

    override fun onFailure(message: String) {
        Toast.makeText(this@MainActivity, message,Toast.LENGTH_LONG).show()
        progressMain.visibility = View.GONE
    }
}