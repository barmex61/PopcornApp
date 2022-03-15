package com.fatih.popcornapp.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.fatih.popcornapp.model.RoomEntity
import com.fatih.popcornapp.repositories.ModelRepositories
import com.fatih.popcornapp.repositories.ModelRepositoriesInterface
import com.fatih.popcornapp.resource.Resource
import com.fatih.popcornapp.room.RoomDao
import com.fatih.popcornapp.room.RoomDb
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchListFragmentViewModel @Inject constructor(private val repositories: ModelRepositoriesInterface,private val roomDao: RoomDao,application: Application) : AndroidViewModel(application) {

    private val _watchList=repositories.getAllTvShow()
    val watchList:LiveData<List<RoomEntity>>
        get() = _watchList

}