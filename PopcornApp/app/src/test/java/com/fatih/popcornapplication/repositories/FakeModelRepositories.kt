package com.fatih.popcornapplication.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fatih.popcornapplication.model.*
import com.fatih.popcornapplication.resource.Resource

class FakeModelRepositories:ModelRepositoriesInterface {

    private val databaseList= mutableListOf<RoomEntity>()
    private val _databaseList=MutableLiveData<List<RoomEntity>>(databaseList)

    private val tvShowDetailList= mutableListOf<TvShowDetail>()
    private val _tvShowDetailList=MutableLiveData<List<TvShowDetail>>(tvShowDetailList)

    private val movieDetailList= mutableListOf<MovieDetail>()
    private val _movieDetailList=MutableLiveData<List<MovieDetail>>(movieDetailList)

    private val movieImages= mutableListOf<MovieImages>()
    private val _movieImages=MutableLiveData<List<MovieImages>>(movieImages)

    private val tvShowImages= mutableListOf<TvShowImages>()
    private val _tvShowImages=MutableLiveData<List<TvShowImages>>(tvShowImages)


    override suspend fun addTvShow(roomEntity: RoomEntity) {
        databaseList.add(roomEntity)
        refreshDetail()
    }

    override suspend fun deleteTvShow(roomEntity: RoomEntity) {
        databaseList.remove(roomEntity)
        refreshDetail()
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
        addMovieDetails()
        return if(_movieDetailList.value?.contains(MovieDetail("sas",1, listOf(),"s",id,"s","s","s","s",2.2,"s","s",1,1,"s",2.2))==true){
                Resource.success(MovieDetail("sas",1, listOf(),"s",id,"s","s","s","s",2.2,"s","s",1,1,"s",2.2))
        }else{
            Resource.error(null,"Error")
        }
    }

    override suspend fun getTvShowDetail(id: Int, api_key: String): Resource<TvShowDetail> {
        addTvShowDetails()
        return if(_tvShowDetailList.value?.contains(TvShowDetail("SS","ss","ff","f", listOf(),"ss",5.5,55,"ss", listOf(),"ss",
                listOf(),"ss",id)) == true
        ){
               Resource.success(TvShowDetail("SS","ss","ff","f", listOf(),"ss",5.5,55,"ss", listOf(),"ss",
                    listOf(),"ss",id))
        }else{
           Resource.error(null,"Error")
        }
    }

    override suspend fun getMovieImages(id: Int): Resource<MovieImages> {
        addMovieImages()
        return if(_movieImages.value?.contains(MovieImages(id, listOf()))==true){
                Resource.success(MovieImages(id, listOf()))
        }else{
            Resource.error(null,null)
        }
    }

    override suspend fun getTvShowImages(id: Int): Resource<TvShowImages> {
        addTvShowImages()
        return if(_tvShowImages.value?.contains(TvShowImages(id, listOf()))==true){
                Resource.success(TvShowImages(id, listOf()))
        }else{
                Resource.error(null,null)
        }
    }

    override suspend fun search(name: String, api_key: String, query: String, page: Int): Resource<SearchModel> {
        return Resource.success(SearchModel(page, listOf(),55,55))
    }

    override suspend fun getVideos(name: String, id: Int): Resource<VideoResponse> {
            return Resource.success(VideoResponse(id, listOf()))
    }

    private fun addTvShowDetails(){
        val dummy=TvShowDetail("SS","ss","ff","f", listOf(),"ss",5.5,55,"ss", listOf(),"ss",
            listOf(),"ss",1)
        val dummy2=TvShowDetail("SS","ss","ff","f", listOf(),"ss",5.5,55,"ss", listOf(),"ss",
            listOf(),"ss",2)
        tvShowDetailList.add(dummy)
        tvShowDetailList.add(dummy2)
        refreshDetail()
    }
    private fun addMovieDetails(){
        val dummy=MovieDetail("sas",1, listOf(),"s",1,"s","s","s","s",2.2,"s","s",1,1,"s",2.2)
        val dummy2=MovieDetail("sas",1, listOf(),"s",2,"s","s","s","s",2.2,"s","s",1,1,"s",2.2)
        movieDetailList.add(dummy)
        movieDetailList.add(dummy2)
        refreshDetail()
    }
    private fun addMovieImages(){
        val dummy=MovieImages(1, listOf())
        val dummy2=MovieImages(2, listOf())
        movieImages.add(dummy)
        movieImages.add(dummy2)
        refreshDetail()
    }
    private fun addTvShowImages(){
        val dummy=TvShowImages(1, listOf())
        val dummy2=TvShowImages(2, listOf())
        tvShowImages.add(dummy)
        tvShowImages.add(dummy2)
        refreshDetail()
        }
    private fun refreshDetail(){
        _databaseList.postValue(databaseList)
        _tvShowDetailList.postValue(tvShowDetailList)
        _movieDetailList.postValue(movieDetailList)
        _tvShowImages.postValue(tvShowImages)
        _movieImages.postValue(movieImages)
    }
}