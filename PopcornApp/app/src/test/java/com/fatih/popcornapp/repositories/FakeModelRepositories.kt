package com.fatih.popcornapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fatih.popcornapp.model.*
import com.fatih.popcornapp.resource.Resource

class FakeModelRepositories:ModelRepositoriesInterface {

    private val databaseList= mutableListOf<RoomEntity>()
    private val _databaseList=MutableLiveData<List<RoomEntity>>(databaseList)

    override suspend fun addTvShow(roomEntity: RoomEntity) {
        databaseList.add(roomEntity)
        refreshData()
    }

    override suspend fun deleteTvShow(roomEntity: RoomEntity) {
        databaseList.remove(roomEntity)
        refreshData()
    }

    override fun getAllTvShow(): LiveData<List<RoomEntity>> {
        return _databaseList
    }

    override suspend fun getSelectedTvShow(idInput: Int): RoomEntity {
        return RoomEntity("SAD","25.12.2012",5.6,true,5)
    }

    override suspend fun getMostPopularMovies(page: Int): Resource<MostPopularMovies> {
        TODO("Not yet implemented")
    }

    override suspend fun getMostPopularTvShows(page: Int): Resource<MostPopularTvShows> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieDetail(id: Int, api_key: String): Resource<MovieDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun getTvShowDetail(id: Int, api_key: String): Resource<TvShowDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieImages(id: Int): Resource<MovieImages> {
        TODO("Not yet implemented")
    }

    override suspend fun getTvShowImages(id: Int): Resource<TvShowImages> {
        TODO("Not yet implemented")
    }

    override suspend fun search(
        name: String,
        api_key: String,
        query: String,
        page: Int
    ): Resource<SearchModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getVideos(name: String, id: Int): Resource<VideoResponse> {
        TODO("Not yet implemented")
    }
    private fun refreshData(){
        _databaseList.postValue(databaseList)
    }
}