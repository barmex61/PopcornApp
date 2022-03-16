package com.fatih.popcornapplication.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity
data class MovieDetail(
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("budget")
    val budget: Int,
    @SerializedName("genres")
    val movieGenres: List<MovieGenre>,
    @SerializedName("homepage")
    val homepage: String,
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("imdb_id")
    val imdbİd: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("popularity")
    val popularity: Double,
    @ColumnInfo(name = "posterPath")
    @SerializedName("poster_path")
    val posterPath: String,
    @ColumnInfo(name = "releaseDate")
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("revenue")
    val revenue: Int,
    @SerializedName("runtime")
    val runtime: Int,
    @SerializedName("status")
    val status: String,
    @ColumnInfo(name = "voteAverage")
    @SerializedName("vote_average")
    val voteAverage: Double
)