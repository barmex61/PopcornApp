package com.fatih.popcornapplication.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fatih.popcornapplication.model.*
import com.fatih.popcornapplication.resource.Resource

class FakeModelRepositoriesTest:ModelRepositoriesInterface {

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

    override suspend fun getSelectedTvShow(idInput: Int): RoomEntity? {
        return _databaseList.value?.get(idInput)
    }

    override suspend fun getMovies(page: Int, sort_by: String, genres: String): Resource<MostPopularMovies> {
        return Resource.success(MostPopularMovies(page, listOf(),44,44))
    }

    override suspend fun getTvShows(page: Int, sort_by: String, genres: String): Resource<MostPopularTvShows> {
        return Resource.success(MostPopularTvShows(page, listOf(),55,44))
    }

    override suspend fun getMovieDetail(id: Int, api_key: String): Resource<MovieDetail> {
        return Resource.success(MovieDetail("ss",55, listOf(),"sad",55,"saa","sa","sadsa","sadas",5.2,"asd","asd",44,55,"asd",5.4))
    }

    override suspend fun getTvShowDetail(id: Int, api_key: String): Resource<TvShowDetail> {
        return Resource.success(TvShowDetail("SS","ss","ff","f", listOf(),"ss",5.5,55,"ss", listOf(),"ss",
            listOf(),"ss",55))
    }

    override suspend fun getMovieImages(id: Int): Resource<MovieImages> {
        return Resource.success(MovieImages(id, listOf()))
    }

    override suspend fun getTvShowImages(id: Int): Resource<TvShowImages> {
        return Resource.success(TvShowImages(id, listOf()))
    }

    override suspend fun search(
        name: String,
        api_key: String,
        query: String,
        page: Int
    ): Resource<SearchModel> {
        return Resource.success(SearchModel(page, listOf(),55,55))
    }

    override suspend fun getVideos(name: String, id: Int): Resource<VideoResponse> {
            return Resource.success(VideoResponse(id, listOf()))
    }
    private fun refreshData(){
        _databaseList.postValue(databaseList)
    }
}