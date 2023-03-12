package com.example.netflixremake

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.example.netflixremake.models.Movie

class MovieAdapter(private val movies: List<Movie>,
                   @LayoutRes private val layoutId: Int
): RecyclerView.Adapter<MovieAdapter.movieViewHolder>() {


    class movieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie){
            val imageCover: ImageView = itemView.findViewById(R.id.iv_movie)
            //imageCover.setImageResource(movie.coverPicture)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): movieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return movieViewHolder(view)
    }

    override fun onBindViewHolder(holder: movieViewHolder, position: Int) {
        val currentMovie = movies[position]
        holder.bind(currentMovie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}