package com.fatih.popcornapp.view

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
import com.fatih.popcornapp.R
import com.fatih.popcornapp.adapter.MovieAdapter
import com.fatih.popcornapp.adapter.SearchAdapter
import com.fatih.popcornapp.adapter.TvShowAdapter
import com.fatih.popcornapp.databinding.FragmentMainBinding
import com.fatih.popcornapp.model.ResultMovies
import com.fatih.popcornapp.model.ResultTvShow
import com.fatih.popcornapp.model.SearchResult
import com.fatih.popcornapp.resource.Status
import com.fatih.popcornapp.viewModel.MainFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainFragment : Fragment() {

    private  var movieList=ArrayList<ResultMovies>()
    private  var tvShowList=ArrayList<ResultTvShow>()
    private var searchList=ArrayList<SearchResult>()
    private  lateinit var movieAdapter: MovieAdapter
    private  lateinit var tvShowAdapter:TvShowAdapter
    private lateinit var searchAdapter:SearchAdapter
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
        movieAdapter= MovieAdapter(movieList)
        tvShowAdapter= TvShowAdapter(tvShowList)
        searchAdapter= SearchAdapter(searchList)
        if(isItInMovieList==true){
            movieButtonClicked()
        }else{
            tvShowButtonClicked()
        }
        binding.moviesRecyclerView.setHasFixedSize(true)
        binding.moviesRecyclerView.layoutManager= GridLayoutManager(requireContext(),3)
        binding.moviesRecyclerView.setOnScrollChangeListener(object :View.OnScrollChangeListener{
            override fun onScrollChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int) {
                if(binding.layoutButtons.y>=-88f&&binding.layoutButtons.y<=168f){
                    binding.layoutButtons.y=binding.layoutButtons.y+p4.toFloat()
                    binding.viewLayout.y=binding.viewLayout.y+p4.toFloat()

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
                        if(searchText.isEmpty()){
                            if(isItInMovieList==true){
                                observeMovieLiveData(currentPage)

                            }else{
                                observeTvShowLiveData(currentPage)
                            }
                        } else{
                            if(isItInMovieList==true){
                                observeSearchLiveData(searchName!!,searchText,currentPage)
                            }else{
                                observeSearchLiveData(searchName!!,searchText,currentPage)
                            }
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
                    searchList.clear()
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
                                totalAvailablePages=it.totalPages
                            }


                        }
                    }
                }

            }
          }
    private fun observeSearchLiveData(name:String,query:String,page:Int){
        if(isBinded==false){
            binding.moviesRecyclerView.adapter=searchAdapter
            isBinded=true
        }
        viewModel.search(name,query,page).observe(viewLifecycleOwner){ resource->
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
                                if(currentPage==1){
                                    searchAdapter.updateInfo(isItInMovieList!!)
                                    searchList.clear()
                                    searchAdapter.notifyDataSetChanged()
                                }
                                movieList.clear()
                                tvShowList.clear()
                                oldCount=searchList.size
                                searchList.addAll(it.searchResults)
                                it.searchResults.forEach {
                                    println(it.backdropPath)
                                }
                                searchAdapter.updateInfo(isItInMovieList!!)
                                searchAdapter.notifyItemRangeInserted(oldCount,searchList.size)

                            }
                        }
                    }
                }
            }

    }
    private fun observeTvShowLiveData(currentPage: Int){
        if(searchText.isEmpty()){
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
                            tvShowList.addAll(it.results)
                            tvShowAdapter.notifyItemRangeInserted(oldCount,tvShowList.size)
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
        tvShowList.clear()
        movieList.clear()
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
        movieList.clear()
        tvShowList.clear()
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