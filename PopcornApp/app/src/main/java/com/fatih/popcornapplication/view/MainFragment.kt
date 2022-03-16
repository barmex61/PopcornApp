package com.fatih.popcornapplication.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.fatih.popcornapplication.R
import com.fatih.popcornapplication.adapter.MovieAdapter
import com.fatih.popcornapplication.adapter.SearchAdapter
import com.fatih.popcornapplication.adapter.TvShowAdapter
import com.fatih.popcornapplication.databinding.FragmentMainBinding
import com.fatih.popcornapplication.resource.Status
import com.fatih.popcornapplication.viewModel.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment @Inject constructor(private val tvShowAdapter:TvShowAdapter,private val searchAdapter: SearchAdapter,private val movieAdapter: MovieAdapter): Fragment() {

    private  lateinit var viewModel: MainFragmentViewModel
    private var currentPage=1
    private var totalAvailablePages=1
    private lateinit var binding:FragmentMainBinding
    private var isItInMovieList:Boolean?=true
    private var oldCount=0
    private var boolean=false
    private var searchText=""
    private var searchName:String?=null
    private var isBinded:Boolean?=false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false)
        doInitialization()
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        binding.searchText.text.clear()
        binding.searchText.clearFocus()
    }

    @SuppressLint("PrivateResource")
    private fun doInitialization(){

        viewModel= ViewModelProvider(this)[MainFragmentViewModel::class.java]
        if(isItInMovieList==true){
            movieButtonClicked()
        }else{
            tvShowButtonClicked()
        }
        binding.moviesRecyclerView.setHasFixedSize(true)
        binding.moviesRecyclerView.layoutManager= GridLayoutManager(requireContext(),3)
        binding.moviesRecyclerView.setOnScrollChangeListener { _, _, _, _, p4 ->
            if (binding.layoutButtons.y >= -88f && binding.layoutButtons.y <= 168f) {
                binding.layoutButtons.y = binding.layoutButtons.y + p4.toFloat()
                binding.viewLayout.y = binding.viewLayout.y + p4.toFloat()

                if (binding.layoutButtons.y < -88f) {
                    binding.layoutButtons.y = -88f
                    binding.viewLayout.y = -28f
                }
                if (binding.layoutButtons.y > 168f) {
                    binding.layoutButtons.y = 168f
                    binding.viewLayout.y = 248f
                }
            }

            if (!binding.moviesRecyclerView.canScrollVertically(1)) {
                if (currentPage <= totalAvailablePages) {
                    currentPage++
                    if (searchText.isEmpty()) {
                        if (isItInMovieList == true) {
                            observeMovieLiveData(currentPage)

                        } else {
                            observeTvShowLiveData(currentPage)
                        }
                    } else {
                        if (isItInMovieList == true) {
                            observeSearchLiveData(searchName!!, searchText, currentPage)
                        } else {
                            observeSearchLiveData(searchName!!, searchText, currentPage)
                        }
                    }

                }
            }
        }
        binding.shareImage.setOnClickListener { findNavController().navigate(MainFragmentDirections.actionMainFragmentToWatchListFragment()) }

        binding.movieButton.setOnClickListener {
            movieButtonClicked()
        }
        binding.tvShowButton.setOnClickListener {
            tvShowButtonClicked()
        }
        binding.searchImage.setOnClickListener {
                binding.searchText.visibility=View.VISIBLE
                binding.headerText.visibility=View.GONE
                binding.searchText.requestFocus()
                binding.searchText.isFocusableInTouchMode=true
                binding.menuImage.setImageResource(R.drawable.ic_back)
                boolean=true
            }
        binding.menuImage.setOnClickListener {
                if(boolean){
                    binding.searchText.text.clear()
                    binding.searchText.clearFocus()
                    binding.searchText.visibility=View.GONE
                    binding.headerText.visibility=View.VISIBLE
                    binding.menuImage.setImageResource(R.drawable.ic_menu)
                    boolean=false
                }else{
                    binding.drawableLayout.openDrawer(GravityCompat.START)
                }
        }
        binding.searchText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            @SuppressLint("NotifyDataSetChanged")
            override fun afterTextChanged(p0: Editable?) {
                searchText=p0.toString().trim()
                if(p0.toString().trim().isNotEmpty()){
                    CoroutineScope(Dispatchers.Main).launch {

                        observeSearchLiveData(searchName!!,p0.toString().trim(),currentPage)
                        currentPage=1
                        totalAvailablePages=1
                    }
                }else{
                    isBinded=false
                    searchAdapter.searchList= listOf()
                    searchAdapter.notifyDataSetChanged()
                    if(isItInMovieList==true){
                        movieButtonClicked()
                    }else{
                        tvShowButtonClicked()
                    }
                }
            }

        })

    }
    private fun observeMovieLiveData(currentPage:Int){
            if(searchText.isEmpty()){
                viewModel.getMostPopularMovies(currentPage)
                viewModel.mostPopularMovies.observe(viewLifecycleOwner){ resource->
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
                                oldCount=movieAdapter.movieList.size
                                movieAdapter.movieList=movieAdapter.movieList+it.results
                                movieAdapter.notifyItemRangeInserted(oldCount,movieAdapter.movieList.size)
                                binding.isLoading=false
                                totalAvailablePages=it.totalPages
                            }


                        }
                    }
                }

            }
          }

    private fun observeSearchLiveData(name:String, query:String, page:Int){
        if(isBinded==false){
            binding.moviesRecyclerView.adapter=searchAdapter
            isBinded=true
        }
        viewModel.search(name,query,page)
        viewModel.searchList.observe(viewLifecycleOwner){ resource->
                if(resource!=null){
                    when(resource.status){
                        Status.LOADING->{
                            binding.isLoading=true

                        }
                        Status.ERROR->{
                            binding.isLoading=false
                            Toast.makeText(requireContext(),"Error",Toast.LENGTH_SHORT).show()
                            binding.moviesRecyclerView.visibility=View.GONE
                        }
                        Status.SUCCESS->{
                            resource.data?.let {
                                binding.isLoading=false
                                binding.moviesRecyclerView.visibility=View.VISIBLE
                                totalAvailablePages=it.totalPages
                                movieAdapter.movieList= listOf()
                                tvShowAdapter.tvShowList= listOf()
                                searchAdapter.searchList=it.searchResults
                                searchAdapter.updateInfo(isItInMovieList!!)
                            }
                        }
                    }
                }
            }
    }
    private fun observeTvShowLiveData(currentPage: Int){
        if(searchText.isEmpty()){
            binding.isLoading=true
            viewModel.getMostPopularTvShows(currentPage)
            viewModel.mostPopularTvShows.observe(viewLifecycleOwner){ resource->
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

                            oldCount=tvShowAdapter.tvShowList.size
                            tvShowAdapter.tvShowList+=it.results
                            tvShowAdapter.notifyItemRangeInserted(oldCount,tvShowAdapter.tvShowList.size)
                            totalAvailablePages=it.totalPages
                        }
                    }
                }
            }
        }
    }
    @SuppressLint("PrivateResource")
    private fun movieButtonClicked(){

        isItInMovieList=true
        tvShowAdapter.tvShowList= listOf()
        movieAdapter.movieList= listOf()
        currentPage=1
        searchName="movie"

        if(searchText.isEmpty()){
            binding.moviesRecyclerView.adapter=movieAdapter
            observeMovieLiveData(currentPage)
        }else{
            observeSearchLiveData(searchName!!,searchText,currentPage)
        }

        binding.movieButtonSelect.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.teal_700))
        binding.tvShowButtonSelect.setBackgroundColor(
            ContextCompat.getColor(requireContext(),
                com.google.android.material.R.color.mtrl_btn_transparent_bg_color))
    }
    @SuppressLint("PrivateResource")
    private fun tvShowButtonClicked(){

        isItInMovieList=false
        movieAdapter.movieList= listOf()
        tvShowAdapter.tvShowList= listOf()
        searchName="tv"
        currentPage=1
        if(searchText.isEmpty()){
            binding.moviesRecyclerView.adapter=tvShowAdapter
            observeTvShowLiveData(currentPage)
        }else{
            observeSearchLiveData(searchName!!,searchText,currentPage)
        }

        binding.tvShowButtonSelect.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.teal_700))
        binding.movieButtonSelect.setBackgroundColor(
            ContextCompat.getColor(requireContext(),
                com.google.android.material.R.color.mtrl_btn_transparent_bg_color))
    }

}