package com.fatih.popcornapplication.model


import com.google.gson.annotations.SerializedName

data class TvShowGenre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)