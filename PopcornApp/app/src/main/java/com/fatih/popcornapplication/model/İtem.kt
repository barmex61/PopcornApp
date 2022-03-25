package com.fatih.popcornapplication.model


import com.google.gson.annotations.SerializedName

data class İtem(
    @SerializedName("contentDetails")
    val contentDetails: ContentDetails,
    @SerializedName("etag")
    val etag: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("snippet")
    val snippet: Snippet,
    @SerializedName("statistics")
    val statistics: Statistics
)