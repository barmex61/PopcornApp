package com.fatih.popcornapp.viewModel

import androidx.lifecycle.*
import com.fatih.popcornapp.model.*
import com.fatih.popcornapp.repositories.ModelRepositoriesInterface
import com.fatih.popcornapp.resource.Resource
import com.fatih.popcornapp.util.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsFragmentViewModel @Inject constructor(private val repositories: ModelRepositoriesInterface) : ViewModel() {

    private val _movieDetails=MutableLiveData<Resource<MovieDetail>>()
    val movieDetails:LiveData<Resource<MovieDetail>>
        get() = _movieDetails

    private val _tvShowDetails=MutableLiveData<Resource<TvShowDetail>>()
    val tvShowDetails:LiveData<Resource<TvShowDetail>>
        get() = _tvShowDetails

    private val _movieImages=MutableLiveData<Resource<MovieImages>>()
    val movieImages:LiveData<Resource<MovieImages>>
        get() = _movieImages

    private val _tvShowImages=MutableLiveData<Resource<TvShowImages>>()
    val tvShowImages:LiveData<Resource<TvShowImages>>
        get() = _tvShowImages

    private val _videos=MutableLiveData<Resource<VideoResponse>>()
    val videos:LiveData<Resource<VideoResponse>>
        get() = _videos

    private val _controlMessage=MutableLiveData<Resource<String>>()
    val controlMessage:LiveData<Resource<String>>
        get() = _controlMessage


    val roomEntity=MutableLiveData<RoomEntity>()


    fun getMovieDetails(id:Int)= viewModelScope.launch{
        _movieDetails.value= Resource.loading(null)
        try {
            _movieDetails.value=repositories.getMovieDetail(id, API_KEY)
        }catch (e:Exception){
            _movieDetails.value= Resource.error(null,e.message)
        }
    }
    fun getTvShowDetails(id:Int)=viewModelScope.launch{

        _tvShowDetails.value= Resource.loading(null)
        try {
            _tvShowDetails.value=repositories.getTvShowDetail(id, API_KEY)
        }catch (e:Exception){
            _tvShowDetails.value= Resource.error(null,e.message)
        }
    }
    fun getMovieImages(id:Int)= viewModelScope.launch{
        _movieImages.value= Resource.loading(null)
        try {
           _movieImages.value=repositories.getMovieImages(id)
        }catch (e:Exception){
            _movieImages.value= Resource.error(null,e.message)
        }
    }
    fun getTvShowImages(id:Int)=viewModelScope.launch{
        _tvShowImages.value= Resource.loading(null)
        try {
            _tvShowImages.value=repositories.getTvShowImages(id)
        }catch (e:Exception){
            _tvShowImages.value=Resource.error(null,e.message)
        }
    }
    fun isItInDatabase(id:Int)=viewModelScope.launch(Dispatchers.Main){
            _controlMessage.value= Resource.error("message","sa")
            try {
                repositories.getSelectedTvShow(id)?.let {

                    roomEntity.value=it
                    _controlMessage.postValue( Resource.success("Success"))

                }

            }catch (e:Exception){
                _controlMessage.postValue(Resource.error(e.message,"Failed"))
            }

    }
    fun addTvShowIntoDatabase(roomEntity: RoomEntity)=viewModelScope.launch{

            try {
                repositories.addTvShow(roomEntity)
                _controlMessage.value= Resource.success("Success")
            }catch (e:Exception){
               _controlMessage.value= Resource.error(null,e.message)
            }
    }
    fun deleteTvShowFromDatabase(roomEntity: RoomEntity){
        viewModelScope.launch(Dispatchers.Default){
            try {
                repositories.deleteTvShow(roomEntity)
            }catch (e:Exception){
                println(e.message)
            }
        }
    }
    fun getVideos(name:String,id:Int)=viewModelScope.launch{
        _videos.value= Resource.loading(null)
        try {
            _videos.value=repositories.getVideos(name,id)
        }catch (e:Exception){
            _videos.value= Resource.error(null,e.message)
        }
    }

}
