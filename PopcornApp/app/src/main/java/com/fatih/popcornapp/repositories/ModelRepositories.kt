package com.fatih.popcornapp.repositories

import androidx.lifecycle.LiveData
import com.fatih.popcornapp.model.*
import com.fatih.popcornapp.resource.Resource
import com.fatih.popcornapp.room.RoomDao
import com.fatih.popcornapp.service.MovieApi
import com.fatih.popcornapp.util.API_KEY
import javax.inject.Inject

class ModelRepositories @Inject constructor(private val roomDao: RoomDao,private val movieApi: MovieApi):ModelRepositoriesInterface {
    override suspend fun addTvShow(roomEntity: RoomEntity) {
       roomDao.addTvShow(roomEntity)
    }

    override suspend fun deleteTvShow(roomEntity: RoomEntity) {
        roomDao.deleteTvShow(roomEntity)
    }

    override fun getAllTvShow(): LiveData<List<RoomEntity>> {
        return roomDao.getAllTvShow()
    }

    override suspend fun getSelectedTvShow(idInput: Int): RoomEntity? {
        return roomDao.getSelectedTvShow(idInput)
    }

    override suspend fun getMostPopularMovies(page: Int): Resource<MostPopularMovies> {
        return if(movieApi.getMostPopularMovies(page).isSuccessful){
            try {
                movieApi.getMostPopularMovies(page).body()?.let {
                    Resource.success(it)
                }?: Resource.error(null,"No Data")
            }catch (e:Exception){
                Resource.error(null,e.message)
            }
        }else{
            Resource.error(null,"Response is not successfully")
        }
    }

    override suspend fun getMostPopularTvShows(page: Int): Resource<MostPopularTvShows> {
        return if(movieApi.getMostPopularTvShows(page).isSuccessful){
            try {
                movieApi.getMostPopularTvShows(page).body()?.let {
                    Resource.success(it)
                }?: Resource.error(null,"No Data")
            }catch (e:Exception){
                Resource.error(null,e.message)
            }
        }else{
            Resource.error(null,"Response is not successfully")
        }
    }

    override suspend fun getMovieDetail(id: Int, api_key: String): Resource<MovieDetail> {
        return if(movieApi.getMovieDetails(id, API_KEY).isSuccessful){
            try {
                movieApi.getMovieDetails(id, API_KEY).body()?.let {
                    Resource.success(it)
                }?: Resource.error(null,"No Data")
            }catch (e:Exception){
                Resource.error(null,e.message)
            }
        }else{
            Resource.error(null,"Response is not successfully")
        }
    }

    override suspend fun getTvShowDetail(id: Int, api_key: String): Resource<TvShowDetail> {
        return if(movieApi.getTvShowDetails(id, API_KEY).isSuccessful){
            try {
                movieApi.getTvShowDetails(id, API_KEY).body()?.let {
                    Resource.success(it)
                }?: Resource.error(null,"No Data")
            }catch (e:Exception){
                Resource.error(null,e.message)
            }
        }else{
            Resource.error(null,"Response is not successfully")
        }
    }

    override suspend fun getMovieImages(id: Int): Resource<MovieImages> {
        return if(movieApi.getMovieImages(id).isSuccessful){
            try {
                movieApi.getMovieImages(id).body()?.let {
                    Resource.success(it)
                }?: Resource.error(null,"No Data")
            }catch (e:Exception){
                Resource.error(null,e.message)
            }
        }else{
            return Resource.error(null,"Response is not successfully")
        }
    }

    override suspend fun getTvShowImages(id: Int): Resource<TvShowImages> {
        return if(movieApi.getTvShowImages(id).isSuccessful){
            try {
                movieApi.getTvShowImages(id).body()?.let {
                    Resource.success(it)
                }?: Resource.error(null,"No Data")
            }catch (e:Exception){
                Resource.error(null,e.message)
            }
        }else{
            return Resource.error(null,"Response is not successfully")
        }
    }

    override suspend fun search(
        name: String,
        api_key: String,
        query: String,
        page: Int
    ): Resource<SearchModel> {
        return if(movieApi.search(name, API_KEY,query, page).isSuccessful){
            try {
                movieApi.search(name, API_KEY,query, page).body()?.let {
                    Resource.success(it)
                }?:Resource.error(null,"No Data")
            }catch (e:Exception){
                Resource.error(null,e.message)
            }
        }else{
            return Resource.error(null,"Response is not successfully")
        }
    }

    override suspend fun getVideos(name: String, id: Int): Resource<VideoResponse> {
        return if(movieApi.getVideos(name,id).isSuccessful){
            try {
                movieApi.getVideos(name,id).body()?.let {
                    Resource.success(it)
                }?:Resource.error(null,"No Data")
            }catch (e:Exception){
                Resource.error(null,e.message)
            }
        }else{
            return Resource.error(null,"Response is not successfully")
        }
    }
}