package com.fatih.popcornapp.model


import com.google.gson.annotations.SerializedName

data class MostPopularMovies(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<ResultMovies>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)