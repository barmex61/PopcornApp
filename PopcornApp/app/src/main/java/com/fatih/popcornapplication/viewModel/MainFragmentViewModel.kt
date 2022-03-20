package com.fatih.popcornapplication.viewModel

import androidx.lifecycle.*
import com.fatih.popcornapplication.model.MostPopularMovies
import com.fatih.popcornapplication.model.MostPopularTvShows
import com.fatih.popcornapplication.model.SearchModel
import com.fatih.popcornapplication.repositories.ModelRepositoriesInterface
import com.fatih.popcornapplication.resource.Resource
import com.fatih.popcornapplication.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(private val repositories: ModelRepositoriesInterface) : ViewModel() {

    private val _mostPopularMovies=MutableLiveData<Resource<MostPopularMovies>>()
    val mostPopularMovies:LiveData<Resource<MostPopularMovies>>
        get() = _mostPopularMovies

    private val _mostPopularTvShow=MutableLiveData<Resource<MostPopularTvShows>>()
    val mostPopularTvShows:LiveData<Resource<MostPopularTvShows>>
        get() = _mostPopularTvShow

    private val _searchList=MutableLiveData<Resource<SearchModel>>()
    val searchList:LiveData<Resource<SearchModel>>
        get() = _searchList


    fun getMovies(page:Int,sort_by:String,genres:String)=viewModelScope.launch{
        var isGenreValid=false
        var isSortValid=false
        if(sort_by.isEmpty()){
            isSortValid=true
        }else{
            movieSortList.forEach {
                if(sort_by==it){
                    isSortValid=true
                }
            }
        }
        if(genres.isEmpty()){
            isGenreValid=true
        }else{
            genreList.forEach {
                var genre=""
                try {
                    genre=genres.substring(0,genres.indexOf(","))
                }catch (e:Exception){
                }
                if(genre.isNotEmpty()&&genre==it.toString()){
                    isGenreValid=true
                }
            }
        }
        if(isSortValid&&isGenreValid) {
            _mostPopularMovies.value= Resource.loading(null)
            try {
                _mostPopularMovies.value=repositories.getMovies(page,sort_by,genres)
            }catch (e:Exception){
                _mostPopularMovies.value= Resource.error(null,e.message)
            }
        }else{
            _mostPopularMovies.value = Resource.error(null, "Parameters have to be not empty")
        }
    }

    fun getTvShows(page: Int,sort_by: String,genres: String)=viewModelScope.launch{
        var isGenreValid=false
        var isSortValid=false
        if(sort_by.isEmpty()){
            isSortValid=true
        }else{
            tvShowSortList.forEach {
                if(sort_by==it){
                    isSortValid=true
                }
            }
        }
        if(genres.isEmpty()){
            isGenreValid=true
        }else{
            genreList.forEach {
                var genre=""
                try {
                    genre=genres.substring(0,genres.indexOf(","))
                }catch (e:Exception){

                }
                if(genre.isNotEmpty()&&genre==it.toString()){
                    isGenreValid=true
                }
            }
        }
        if(isSortValid&&isGenreValid){
            _mostPopularTvShow.value= Resource.loading(null)
            try {
                _mostPopularTvShow.value=repositories.getTvShows(page,sort_by, genres)
            }catch (e:Exception){
                _mostPopularTvShow.value= Resource.error(null,e.message)
            }
        }else{
            _mostPopularTvShow.value= Resource.error(null,"Parameters have to be not empty")
        }
    }

    fun search(name:String,query:String,page:Int)= viewModelScope.launch{
        if(name.isEmpty()||query.isEmpty()||page.toString().isEmpty()){
            _searchList.value= Resource.error(null,"All lines must filled")
        }else{
            _searchList.value= Resource.loading(null)
            try {
                _searchList.value=repositories.search(name, API_KEY,query,page)
            }catch (e:Exception){
                _searchList.value= Resource.error(null,e.message)
            }
        }
    }

}