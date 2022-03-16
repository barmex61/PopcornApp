package com.fatih.popcornapplication.model


import com.google.gson.annotations.SerializedName

data class MovieImages(
    @SerializedName("id")
    val id: Int,
    @SerializedName("posters")
    val moviePosters: List<MoviePoster>
)