package com.fatih.popcornapplication.viewModel

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.fatih.popcornapplication.R
import com.fatih.popcornapplication.databinding.FragmentDetailsBinding
import com.fatih.popcornapplication.model.*
import com.fatih.popcornapplication.repositories.ModelRepositoriesInterface
import com.fatih.popcornapplication.resource.Resource
import com.fatih.popcornapplication.util.API_KEY
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
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
        if(roomEntity.lastAirDate.isNotEmpty()&&roomEntity.posterPath.isNotEmpty()){
            try {
                repositories.addTvShow(roomEntity)
                _controlMessage.value= Resource.success("Success")
            }catch (e:Exception){
                _controlMessage.value= Resource.error("Error",e.message)
            }
        }else{
            _controlMessage.value= Resource.error(null,"error")
        }

    }
    fun deleteTvShowFromDatabase(roomEntity: RoomEntity)=viewModelScope.launch{
        if(roomEntity.lastAirDate.isNotEmpty()&&roomEntity.posterPath.isNotEmpty()) {
            try {
                repositories.deleteTvShow(roomEntity)
                _controlMessage.value= Resource.success("success")
            } catch (e: Exception) {
                _controlMessage.value= Resource.error("Error",e.message)
            }
        }else{
            _controlMessage.value= Resource.error("error","error")
        }
    }
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
    fun setTints(selectedMovie:MovieDetail?, selectedTvShow:TvShowDetail?, binding:FragmentDetailsBinding, context: Context,vibrantColor:Int){
        binding.color=vibrantColor

        selectedMovie?.let {it->
            binding.genreText.text= it.movieGenres[0].name
            binding.ratingBar.rating= it.voteAverage.toFloat()
            binding.textDescription.text=it.overview
            binding.runtimeText.text=it.runtime.toString()+" min"
            binding.imageUrl=it.backdropPath
            binding.ratingText.text=it.voteAverage.toString()
            binding.nameText.text=it.originalTitle
            binding.yearText.text=it.releaseDate
            binding.episodesImage.visibility= View.INVISIBLE
            binding.episodesText.visibility= View.INVISIBLE
        }
        selectedTvShow?.let { it->
            binding.genreText.text= it.genres[0].name
            binding.ratingBar.rating= it.voteAverage.toFloat()
            binding.textDescription.text=it.overview
            binding.runtimeText.text=it.episodeRunTime[0].toString()+" min"
            binding.imageUrl=it.backdropPath
            binding.ratingText.text=it.voteAverage.toString()
            binding.nameText.text=it.name
            binding.yearText.text=it.lastAirDate
            binding.episodesImage.visibility= View.VISIBLE
            binding.episodesText.visibility= View.VISIBLE
        }

        binding.imgPlay.backgroundTintList= ColorStateList.valueOf(vibrantColor)
        binding.saveImage.backgroundTintList= ColorStateList.valueOf(vibrantColor)
        binding.trailerImage.imageTintList= ColorStateList.valueOf(vibrantColor)
        binding.ratingBar.progressTintList= ColorStateList.valueOf(vibrantColor)
        binding.ratingBar.backgroundTintList=
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
        binding.ratingBar.progressBackgroundTintList=
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))

    }

    fun getImagesFromPicasso(movieImages:ArrayList<MoviePoster>?,tvShowImages:ArrayList<TvShowPoster>?,binding: FragmentDetailsBinding){
        movieImages?.let { it->
            var x=10
            if(it.size<10){
                x=it.size
            }
            CoroutineScope(Dispatchers.IO).launch {
                for(i in 0 until x){
                    val url=it[i].filePath
                    try {
                        withContext(Dispatchers.Main){
                            binding.backgroundImage.alpha=0f
                            Picasso.get().load("https://www.themoviedb.org/t/p/w600_and_h900_bestv2$url").into(binding.backgroundImage,object:
                                Callback {
                                override fun onSuccess() {
                                    binding.backgroundImage.animate().alpha(1f).setDuration(3000).withEndAction {
                                        binding.backgroundImage.animate().alpha(0f).setDuration(2500).start()
                                    }.start()
                                }

                                override fun onError(e: java.lang.Exception?) {
                                    println(e!!.message)
                                }

                            })
                        }

                    }catch (e:Exception){
                        println(e)
                    }
                    delay(5500)
                }

            }
        }
        tvShowImages?.let {it->
            var x=10
            if(it.size<10){
                x=it.size
            }
            CoroutineScope(Dispatchers.IO).launch {
                for(i in 0 until x){
                    val url=it[i].filePath
                    try {
                        withContext(Dispatchers.Main){
                            binding.backgroundImage.alpha=0f
                            Picasso.get().load("https://www.themoviedb.org/t/p/w600_and_h900_bestv2$url").into(binding.backgroundImage,object:
                                Callback {
                                override fun onSuccess() {
                                    binding.backgroundImage.animate().alpha(1f).setDuration(3000).withEndAction {
                                        binding.backgroundImage.animate().alpha(0f).setDuration(2500).start()
                                    }.start()
                                }

                                override fun onError(e: java.lang.Exception?) {
                                    println(e!!.message)
                                }

                            })
                        }

                    }catch (e:Exception){
                        println(e)
                    }
                    delay(5500)
                }

            }
        }
    }

}
