package com.fatih.popcornapplication.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.fatih.popcornapplication.adapter.*
import javax.inject.Inject

class FragmentFactories @Inject constructor(private val youtubeVideoAdapter: YoutubeVideoAdapter,private val tvShowAdapter:TvShowAdapter,private val seasonAdapter: SeasonAdapter,private val searchAdapter: SearchAdapter,private val movieAdapter: MovieAdapter,private val watchListAdapter: WatchListAdapter) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            MainFragment::class.java.name->MainFragment(tvShowAdapter,searchAdapter,movieAdapter)
            DetailsFragment::class.java.name->DetailsFragment(seasonAdapter)
            WatchListFragment::class.java.name->WatchListFragment(watchListAdapter)
            TrailerFragment::class.java.name->TrailerFragment(youtubeVideoAdapter)
            else->super.instantiate(classLoader, className)
        }
    }
}