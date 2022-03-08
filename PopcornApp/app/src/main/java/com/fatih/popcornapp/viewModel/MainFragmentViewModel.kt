package com.fatih.popcornapp.viewModel

import androidx.lifecycle.*
import com.fatih.popcornapp.model.MostPopularMovies
import com.fatih.popcornapp.model.ResultMovies
import com.fatih.popcornapp.resource.Resource
import com.fatih.popcornapp.service.MovieHelper
import com.fatih.popcornapp.service.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainFragmentViewModel: ViewModel() {
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

}