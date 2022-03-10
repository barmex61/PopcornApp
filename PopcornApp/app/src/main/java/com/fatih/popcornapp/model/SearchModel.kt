package com.fatih.popcornapp.model


import com.google.gson.annotations.SerializedName

data class SearchModel(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val searchResults: List<SearchResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)