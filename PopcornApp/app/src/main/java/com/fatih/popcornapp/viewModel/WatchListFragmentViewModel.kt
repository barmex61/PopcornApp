package com.fatih.popcornapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.fatih.popcornapp.resource.Resource
import com.fatih.popcornapp.room.RoomDb
import kotlinx.coroutines.Dispatchers


class WatchListFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val roomDao=RoomDb.invoke(application).roomDao()

    fun getAllWatchListData()= liveData(Dispatchers.Default){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(roomDao.getAllTvShow()))
        }catch (e:Exception){
            emit(Resource.error(null,e.message?:"Error Occurred!"))
        }
    }
}