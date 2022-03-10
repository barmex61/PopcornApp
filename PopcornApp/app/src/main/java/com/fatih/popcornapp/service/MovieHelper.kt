package com.fatih.popcornapp.service

import com.fatih.popcornapp.model.VideoResponse

class MovieHelper(private val movieApi: MovieApi) {
        suspend fun getMostPopularMovies(page:Int)=movieApi.getMostPopularMovies(page)
        suspend fun getMostPopularTvShows(page: Int)=movieApi.getMostPopularTvShows(page)
        suspend fun getMovieDetail(id:Int,api_key:String)=movieApi.getMovieDetails(id,api_key)
        suspend fun getTvShowDetail(id:Int,api_key: String)=movieApi.getTvShowDetails(id,api_key)
        suspend fun getMovieImages(id:Int)=movieApi.getMovieImages(id)
        suspend fun getTvShowImages(id:Int)=movieApi.getTvShowImages(id)
        suspend fun search(name:String,api_key:String,query:String,page:Int)=movieApi.search(name,api_key,query,page)
        suspend fun getVideos(name: String,id: Int): VideoResponse {
                return movieApi.getVideos(name,id)
        }
}