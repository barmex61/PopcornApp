package com.fatih.popcornapp.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.fatih.popcornapp.model.RoomEntity
import com.fatih.popcornapp.resource.Resource
import com.fatih.popcornapp.room.RoomDb
import com.fatih.popcornapp.service.MovieHelper
import com.fatih.popcornapp.service.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val movieHelper=MovieHelper(MovieService.movieApi)
    private val roomDao=RoomDb.invoke(application).roomDao()
    var roomEntity=MutableLiveData<RoomEntity>()
    fun getMovieDetails(id:Int)= liveData(Dispatchers.IO){

            emit(Resource.loading(null))
        try {
            emit(Resource.success(movieHelper.getMovieDetail(id,"ae624ef782f69d5092464dffa234178b")))
        }catch (e:Exception){
            emit(Resource.error(null,e.message?:"Error Occurred"))
        }
    }
    fun getTvShowDetails(id:Int)= liveData(Dispatchers.IO){

        emit(Resource.loading(null))
        try {
            emit(Resource.success(movieHelper.getTvShowDetail(id,"ae624ef782f69d5092464dffa234178b")))
        }catch (e:Exception){
            emit(Resource.error(null,e.message?:"Error Occurred!"))
        }
    }
    fun getMovieImages(id:Int)= liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(movieHelper.getMovieImages(id).moviePosters))
        }catch (e:Exception){
            emit(Resource.error(null,e.message?:"Error Occurred!"))
        }
    }
    fun getTvShowImages(id:Int)= liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(movieHelper.getTvShowImages(id).tvShowPosters))
        }catch (e:Exception){
            emit(Resource.error(null,e.message?:"Error Occurred!"))
        }
    }
    fun isItInDatabase(id:Int){

        viewModelScope.launch(Dispatchers.Default){
            try {
                val room=roomDao.getSelectedTvShow(id)
                withContext(Dispatchers.Main){
                    roomEntity.value=room
                    println(roomEntity.value)
                }
                println(roomEntity)
            }catch (e:Exception){
                println(e.message)
            }
        }

    }
    fun addTvShowIntoDatabase(roomEntity: RoomEntity){
        viewModelScope.launch(Dispatchers.Default){
            try {
                roomDao.addTvShow(roomEntity)
            }catch (e:Exception){
                println(e.message)
            }
        }
    }
    fun deleteTvShowFromDatabase(roomEntity: RoomEntity){
        viewModelScope.launch(Dispatchers.Default){
            try {
                roomDao.deleteTvShow(roomEntity)
            }catch (e:Exception){
                println(e.message)
            }
        }
    }
    fun getVideos(name:String,id:Int)= liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(movieHelper.getVideos(name,id)))
        }catch (e:Exception){
            emit(Resource.error(null,e.message?:"Error Occurred!"))
        }
    }

}
