package com.fatih.popcornapplication.viewModel


import androidx.lifecycle.*
import com.fatih.popcornapplication.model.RoomEntity
import com.fatih.popcornapplication.repositories.ModelRepositoriesInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WatchListFragmentViewModel @Inject constructor(private val repositories: ModelRepositoriesInterface) :ViewModel() {

    private val _watchList=repositories.getAllTvShow()
    val watchList:LiveData<List<RoomEntity>>
        get() = _watchList

   /* fun setBackgroundColor(context:Context,binding: FragmentWatchListBinding){
        val color=ArrayList<Int>()
        color.add(ContextCompat.getColor(context, R.color.black2))
        color.add(ContextCompat.getColor(context, R.color.red))
        color.add(ContextCompat.getColor(context, R.color.nown))
        color.add(ContextCompat.getColor(context, R.color.opakwhite))
        color.add(ContextCompat.getColor(context, R.color.green))
        var i=0
        CoroutineScope(Dispatchers.Default).launch {
            while(i in 0 until color.size){
                binding.layoutHeaderWatchList.setBackgroundColor(color[i])
                i++
                if(i==color.size){
                    i=0
                }
                delay(10000)
            }
        }
    } */
}