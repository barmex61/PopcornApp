package com.fatih.popcornapplication.model


import com.google.gson.annotations.SerializedName

data class YoutubeResponse(
    @SerializedName("etag")
    val etag: String,
    @SerializedName("items")
    val items: List<İtem>,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("pageInfo")
    val pageInfo: PageInfo
)