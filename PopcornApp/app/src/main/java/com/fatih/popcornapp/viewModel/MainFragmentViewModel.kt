package com.fatih.popcornapp.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.fatih.popcornapp.resource.Resource
import com.fatih.popcornapp.service.MovieHelper
import com.fatih.popcornapp.service.MovieService
import kotlinx.coroutines.Dispatchers



class MainFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val movieHelper=MovieHelper(MovieService.movieApi)
    fun getMostPopularMovies(page:Int)= liveData(Dispatchers.IO){

        emit(Resource.loading(null))
                try {
                emit(Resource.success(movieHelper.getMostPopularMovies(page)))

                }catch (e:Exception){
                emit(Resource.error(null,e.message?:"Error Occurred!"))
                }

    }

    fun getMostPopularTvShows(page: Int)= liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(movieHelper.getMostPopularTvShows(page)))
        }catch (e:Exception){
            emit(Resource.error(null,e.message?:"Error Occurred!"))
        }
    }
    fun search(name:String,query:String,page:Int)= liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(movieHelper.search(name,"ae624ef782f69d5092464dffa234178b",query,page)))
        }catch (e:Exception){
            emit(Resource.error(null,e.message?:"Error Occurred !"))
        }
    }

}