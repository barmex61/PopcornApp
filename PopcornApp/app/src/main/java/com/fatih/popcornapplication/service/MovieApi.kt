package com.fatih.popcornapplication.service

import com.fatih.popcornapplication.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    //https://api.themoviedb.org/3/movie/popular?api_key=ae624ef782f69d5092464dffa234178b&page=2
    //https://api.themoviedb.org/3/tv/popular?api_key=ae624ef782f69d5092464dffa234178b&page=1
    //https://api.themoviedb.org/3/search/movie?api_key=ae624ef782f69d5092464dffa234178b&query=Spi&page=1
    //https://api.themoviedb.org/3/tv/85552/videos?api_key=ae624ef782f69d5092464dffa234178b
    @GET("movie/popular?api_key=ae624ef782f69d5092464dffa234178b")
    suspend fun getMostPopularMovies(@Query("page")page:Int):Response<MostPopularMovies>
    @GET("tv/popular?api_key=ae624ef782f69d5092464dffa234178b")
    suspend fun getMostPopularTvShows(@Query("page")page:Int):Response<MostPopularTvShows>
    @GET("movie/{id}")
    suspend fun getMovieDetails(@Path("id")id:Int, @Query("api_key")api_key:String):Response<MovieDetail>
    @GET("tv/{id}")
    suspend fun getTvShowDetails(@Path("id")id:Int,@Query("api_key")api_key: String):Response<TvShowDetail>
    @GET("movie/{id}/images?api_key=ae624ef782f69d5092464dffa234178b")
    suspend fun getMovieImages(@Path("id")id:Int):Response<MovieImages>
    @GET("tv/{id}/images?api_key=ae624ef782f69d5092464dffa234178b")
    suspend fun getTvShowImages(@Path("id")id:Int):Response<TvShowImages>
    @GET("search/{name}")
    suspend fun search(@Path("name")name:String,@Query("api_key")api_key:String,@Query("query")query:String,@Query("page")page:Int):Response<SearchModel>
    @GET("{query}/{id}/videos?api_key=ae624ef782f69d5092464dffa234178b")
    suspend fun getVideos(@Path("query")name:String,@Path("id")id:Int):Response<VideoResponse>
}