package com.fatih.popcornapp.repositories

import androidx.lifecycle.LiveData
import com.fatih.popcornapp.model.*
import com.fatih.popcornapp.resource.Resource

interface ModelRepositoriesInterface {
    suspend fun addTvShow(roomEntity: RoomEntity)
    suspend fun deleteTvShow(roomEntity: RoomEntity)
    fun getAllTvShow():LiveData<List<RoomEntity>>
    suspend fun getSelectedTvShow(idInput:Int):RoomEntity?
    suspend fun getMostPopularMovies(page:Int): Resource<MostPopularMovies>
    suspend fun getMostPopularTvShows(page: Int):Resource< MostPopularTvShows>
    suspend fun getMovieDetail(id:Int,api_key:String):Resource< MovieDetail>
    suspend fun getTvShowDetail(id:Int,api_key: String):Resource<TvShowDetail>
    suspend fun getMovieImages(id:Int):Resource<MovieImages>
    suspend fun getTvShowImages(id:Int):Resource<TvShowImages>
    suspend fun search(name:String,api_key:String,query:String,page:Int):Resource<SearchModel>
    suspend fun getVideos(name: String,id: Int): Resource<VideoResponse>
}