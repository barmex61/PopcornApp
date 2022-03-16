package com.fatih.popcornapplication.model


import com.google.gson.annotations.SerializedName

data class TvShowImages(
    @SerializedName("id")
    val id: Int,
    @SerializedName("posters")
    val tvShowPosters: List<TvShowPoster>
)