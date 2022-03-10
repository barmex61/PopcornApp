package com.fatih.popcornapp.model


import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val videoResults: List<VideoResult>
)