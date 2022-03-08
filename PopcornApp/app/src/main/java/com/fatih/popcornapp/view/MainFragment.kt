package com.fatih.popcornapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.fatih.popcornapp.R
import com.fatih.popcornapp.adapter.MovieAdapter
import com.fatih.popcornapp.adapter.TvShowAdapter
import com.fatih.popcornapp.databinding.FragmentMainBinding
import com.fatih.popcornapp.model.ResultMovies
import com.fatih.popcornapp.model.ResultTvShow
import com.fatih.popcornapp.resource.Status
import com.fatih.popcornapp.viewModel.MainFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainFragment : Fragment() {

    private  var movieList=ArrayList<ResultMovies>()
    private  var tvShowList=ArrayList<ResultTvShow>()
    private  lateinit var movieAdapter: MovieAdapter
    private  lateinit var tvShowAdapter:TvShowAdapter
    private  lateinit var viewModel: MainFragmentViewModel
    private var currentPage=1
    private var totalAvailablePages=1
    private lateinit var binding:FragmentMainBinding
    private var isItInMovieList:Boolean?=true
    private var oldCount=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false)
        doInitialization()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    @SuppressLint("PrivateResource")
    private fun doInitialization(){

        viewModel= ViewModelProvider(this)[MainFragmentViewModel::class.java]
        movieAdapter= MovieAdapter(movieList)
        tvShowAdapter= TvShowAdapter(tvShowList)
        if(isItInMovieList==true){
            movieButtonClicked()
        }else{
            tvShowButtonClicked()
        }
        binding.moviesRecyclerView.setHasFixedSize(false)
        binding.moviesRecyclerView.setItemViewCacheSize(40)
        binding.moviesRecyclerView.layoutManager= GridLayoutManager(requireContext(),3)
        binding.moviesRecyclerView.setOnScrollChangeListener(object :View.OnScrollChangeListener{
            override fun onScrollChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int) {
                if(binding.layoutButtons.y>=-88f&&binding.layoutButtons.y<=168f){
                    binding.layoutButtons.y=binding.layoutButtons.y+p4.toFloat()
                    binding.viewLayout.y=binding.viewLayout.y+p4.toFloat()
                    println(binding.viewLayout.y)
                    if(binding.layoutButtons.y<-88f){
                        binding.layoutButtons.y= -88f
                        binding.viewLayout.y=-28f
                    }
                    if(binding.layoutButtons.y>168f){
                        binding.layoutButtons.y=168f
                        binding.viewLayout.y=248f }
                }

                if(!binding.moviesRecyclerView.canScrollVertically(1)){
                    if(currentPage<=totalAvailablePages){
                        currentPage++
                        if(isItInMovieList==true){
                            observeMovieLiveData(currentPage)

                        }else{
                            observeTvShowLiveData(currentPage)
                        }
                    }
                }
            }

        })
        binding.shareImage.setOnClickListener { findNavController().navigate(MainFragmentDirections.actionMainFragmentToWatchListFragment()) }

        binding.movieButton.setOnClickListener {
            movieButtonClicked()
        }
        binding.tvShowButton.setOnClickListener {
            tvShowButtonClicked()
        }

    }
    private fun observeMovieLiveData(currentPage:Int){

            viewModel.getMostPopularMovies(currentPage).observe(viewLifecycleOwner){ resource->
                if (resource!=null){
                    if(resource.status==Status.LOADING){

                        binding.isLoading=true
                    }
                    if(resource.status==Status.ERROR){
                        binding.moviesRecyclerView.visibility=View.INVISIBLE
                        binding.isLoading=false
                    }
                    if(resource.status==Status.SUCCESS){

                            binding.moviesRecyclerView.visibility=View.VISIBLE

                            resource.data?.let {
                                oldCount=movieList.size
                                movieList.addAll(it.results)
                                binding.isLoading=false
                                movieAdapter.notifyItemRangeInserted(oldCount,movieList.size)
                                println("ss")
                                totalAvailablePages=it.totalPages
                            }


                    }
                }
            }


          }


    private fun observeTvShowLiveData(currentPage: Int){

        binding.isLoading=true
        viewModel.getMostPopularTvShows(currentPage).observe(viewLifecycleOwner){ resource->
            if (resource!=null){
                if(resource.status==Status.LOADING){

                    binding.isLoading=true
                }
                if(resource.status==Status.ERROR){
                    binding.moviesRecyclerView.visibility=View.INVISIBLE
                    binding.isLoading=false
                }
                if(resource.status==Status.SUCCESS){
                    binding.moviesRecyclerView.visibility=View.VISIBLE
                    binding.isLoading=false
                    resource.data?.let {

                        oldCount=tvShowList.size
                        println(oldCount)
                        tvShowList.addAll(it.results)
                        println(tvShowList.size)
                        tvShowAdapter.notifyItemRangeInserted(oldCount,tvShowList.size)
                        totalAvailablePages=it.totalPages
                    }
                }
            }
        }

    }
    private fun movieButtonClicked(){
        binding.moviesRecyclerView.adapter=movieAdapter
        isItInMovieList=true
        tvShowList.clear()
        movieList.clear()
        currentPage=1
        observeMovieLiveData(currentPage)
        binding.movieButtonSelect.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.teal_700))
        binding.tvShowButtonSelect.setBackgroundColor(
            ContextCompat.getColor(requireContext(),
                com.google.android.material.R.color.mtrl_btn_transparent_bg_color))
    }
    private fun tvShowButtonClicked(){
        binding.moviesRecyclerView.adapter=tvShowAdapter
        isItInMovieList=false
        movieList.clear()
        tvShowList.clear()
        currentPage=1
        observeTvShowLiveData(currentPage)
        binding.tvShowButtonSelect.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.teal_700))
        binding.movieButtonSelect.setBackgroundColor(
            ContextCompat.getColor(requireContext(),
                com.google.android.material.R.color.mtrl_btn_transparent_bg_color))
    }

}