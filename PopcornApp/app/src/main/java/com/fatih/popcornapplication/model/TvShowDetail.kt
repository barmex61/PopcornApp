package com.fatih.popcornapplication.model


import com.google.gson.annotations.SerializedName
    data class TvShowDetail (

    @SerializedName("last_air_date")
    val lastAirDate: String,

    @SerializedName("poster_path")
    val posterPath: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("overview")
    val overview:String,

    @SerializedName("seasons")
    val tvShowSeasons: List<TvShowSeason>,

    @SerializedName("status")
    val status: String,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("vote_count")
    val voteCount: Int,

    @SerializedName("backdrop_path")
    val backdropPath: String,

    @SerializedName("episode_run_time")
    val episodeRunTime: List<Int>,

    @SerializedName("first_air_date")
     val firstAirDate: String,

    @SerializedName("genres")
    val genres: List<TvShowGenre>,

    @SerializedName("homepage")
    val homepage: String,

    @SerializedName("id")
    val id: Int,
)

