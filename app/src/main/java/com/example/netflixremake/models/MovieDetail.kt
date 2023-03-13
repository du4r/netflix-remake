package com.example.netflixremake.models

data class MovieDetail(
    val movie: Movie,
    val similars: List<Movie>
)
