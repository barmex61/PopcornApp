package com.fatih.popcornapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fatih.popcornapplication.model.VideoResponse
import com.fatih.popcornapplication.model.YoutubeResponse
import com.fatih.popcornapplication.repositories.ModelRepositoriesInterface
import com.fatih.popcornapplication.resource.Resource
import com.fatih.popcornapplication.service.MovieApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class TrailerFragmentViewModel @Inject constructor(private val repositories: ModelRepositoriesInterface):ViewModel() {

    private val movieApi=Retrofit.Builder().baseUrl("https://youtube.googleapis.com/youtube/v3/").addConverterFactory(GsonConverterFactory.create()).build().create(MovieApi::class.java)

    private val _videos= MutableLiveData<Resource<VideoResponse>>()
    val videos: LiveData<Resource<VideoResponse>>
        get() = _videos

    private val _youtubeVideos=MutableLiveData<Resource<YoutubeResponse>>()
    val youtubeVideos:LiveData<Resource<YoutubeResponse>>
        get() = _youtubeVideos

    fun getVideos(name:String,id:Int)=viewModelScope.launch{
        if(name.isEmpty()){
            _videos.value= Resource.error(null,"Name is empty")
        }else{
            _videos.value= Resource.loading(null)
            try {
                _videos.value=repositories.getVideos(name,id)
            }catch (e:Exception){
                _videos.value= Resource.error(null,e.message)
            }
        }
    }
    fun getYoutubeVideos(id:String,part:String)=viewModelScope.launch {
            _youtubeVideos.value= Resource.loading(null)
        try {
            _youtubeVideos.value=movieApi.getYoutubeVideoDetails(part, id).body()?.let {
                Resource.success(it)
            }?: Resource.error(null,"No Data!")
        }catch (e:Exception){
            _youtubeVideos.value= Resource.error(null,e.message)
        }
    }
}