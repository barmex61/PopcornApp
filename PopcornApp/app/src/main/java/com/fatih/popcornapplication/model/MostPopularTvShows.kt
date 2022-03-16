package com.fatih.popcornapplication.model


import com.google.gson.annotations.SerializedName

data class MostPopularTvShows(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<ResultTvShow>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)