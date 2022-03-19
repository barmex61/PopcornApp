package com.fatih.popcornapplication.repositories

import androidx.lifecycle.LiveData
import com.fatih.popcornapplication.model.*
import com.fatih.popcornapplication.resource.Resource
import com.fatih.popcornapplication.room.RoomDao
import com.fatih.popcornapplication.service.MovieApi
import com.fatih.popcornapplication.util.API_KEY
import javax.inject.Inject

class ModelRepositories @Inject constructor(private val roomDao: RoomDao, private val movieApi: MovieApi):ModelRepositoriesInterface {
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

    override suspend fun getMovies(page: Int,sort_by:String,genres:String): Resource<MostPopularMovies> {
        return if(movieApi.getMovies(sort_by,page,genres).isSuccessful){
            try {
                movieApi.getMovies(sort_by,page,genres).body()?.let {
                    Resource.success(it)
                }?: Resource.error(null,"No Data")
            }catch (e:Exception){
                Resource.error(null,e.message)
            }
        }else{
            Resource.error(null,"Response is not successfully")
        }
    }

    override suspend fun getTvShows(page: Int,sort_by: String,genres: String): Resource<MostPopularTvShows> {
        return if(movieApi.getTvShows(sort_by,page,genres).isSuccessful){
            try {
                movieApi.getTvShows(sort_by,page,genres).body()?.let {
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