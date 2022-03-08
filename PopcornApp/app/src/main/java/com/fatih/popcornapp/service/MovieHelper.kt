package com.fatih.popcornapp.service

class MovieHelper(private val movieApi: MovieApi) {
        suspend fun getMostPopularMovies(page:Int)=movieApi.getMostPopularMovies(page)
        suspend fun getMostPopularTvShows(page: Int)=movieApi.getMostPopularTvShows(page)
        suspend fun getMovieDetail(id:Int,api_key:String)=movieApi.getMovieDetails(id,api_key)
        suspend fun getTvShowDetail(id:Int,api_key: String)=movieApi.getTvShowDetails(id,api_key)
        suspend fun getMovieImages(id:Int)=movieApi.getMovieImages(id)
        suspend fun getTvShowImages(id:Int)=movieApi.getTvShowImages(id)
}