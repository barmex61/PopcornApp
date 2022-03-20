package com.fatih.popcornapplication.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
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
import com.fatih.popcornapplication.util.*
import com.fatih.popcornapplication.viewModel.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment @Inject constructor(private val tvShowAdapter:TvShowAdapter,private val searchAdapter: SearchAdapter,val movieAdapter: MovieAdapter): Fragment() {

    lateinit var viewModel: MainFragmentViewModel
    private var currentPage=1
    private var totalAvailablePages=1
    private lateinit var binding:FragmentMainBinding
    private var isItInMovieList:Boolean?=true
    private var oldCount=0
    private var boolean=false
    private var searchText=""
    private var searchName:String?=null
    private var isBinded:Boolean?=false
    private var tvShowSortString="popularity.desc"
    private var movieSortString="popularity.desc"
    private var movieGenres=""
    private var tvShowGenres=""
    private var searchGenre=""
    private var indexPosition=0
    private var tvShowSortPosition=0
    private var movieSortPosition=0


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

        binding.navigationView.setNavigationItemSelectedListener {
            setNavigation(it)
            return@setNavigationItemSelectedListener false
        }

        viewModel= ViewModelProvider(this)[MainFragmentViewModel::class.java]
        if(isItInMovieList==true){
            movieButtonClicked()
        }else{
            tvShowButtonClicked()
        }
        binding.moviesRecyclerView.setHasFixedSize(true)
        binding.moviesRecyclerView.layoutManager= GridLayoutManager(requireContext(),3)
        binding.moviesRecyclerView.setOnScrollChangeListener { _, _, _, _, _ ->

            if (!binding.moviesRecyclerView.canScrollVertically(1)) {
                if (currentPage <= totalAvailablePages) {
                    currentPage++
                    if (searchText.isEmpty()) {
                        if (isItInMovieList == true) {
                            observeMovieLiveData(currentPage,movieSortString, movieGenres)

                        } else {
                            observeTvShowLiveData(currentPage,tvShowSortString, tvShowGenres)
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
    private fun observeMovieLiveData(currentPage:Int,sort_by:String,genres:String){
            if(searchText.isEmpty()){
                viewModel.getMovies(currentPage,sort_by,genres)
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

                                if(searchGenre==genres){
                                    oldCount=movieAdapter.movieList.size
                                    movieAdapter.movieList=movieAdapter.movieList+it.results
                                    movieAdapter.notifyItemRangeInserted(oldCount,movieAdapter.movieList.size)
                                    binding.isLoading=false
                                    totalAvailablePages=it.totalPages
                                }else{
                                    movieAdapter.movieList=it.results
                                    binding.isLoading=false
                                    searchGenre=genres
                                }

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
    private fun observeTvShowLiveData(currentPage: Int,sort_by: String,genres: String){
        if(searchText.isEmpty()){
            binding.isLoading=true
            viewModel.getTvShows(currentPage,sort_by, genres)
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
                            if(searchGenre==genres){
                                oldCount=tvShowAdapter.tvShowList.size
                                tvShowAdapter.tvShowList+=it.results
                                tvShowAdapter.notifyItemRangeInserted(oldCount,tvShowAdapter.tvShowList.size)
                                totalAvailablePages=it.totalPages
                            }else{
                                tvShowAdapter.tvShowList=it.results
                                binding.isLoading=false
                                searchGenre=genres
                            }
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
            observeMovieLiveData(currentPage,movieSortString,movieGenres)
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
            observeTvShowLiveData(currentPage,tvShowSortString,tvShowGenres)
        }else{
            observeSearchLiveData(searchName!!,searchText,currentPage)
        }

        binding.tvShowButtonSelect.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.teal_700))
        binding.movieButtonSelect.setBackgroundColor(
            ContextCompat.getColor(requireContext(),
                com.google.android.material.R.color.mtrl_btn_transparent_bg_color))
    }
    private fun setNavigation(it:MenuItem){
        when(it.itemId){
            R.id.movies->{
                val list= arrayOf("Movie","Tv Show")
                val alertDialog=AlertDialog.Builder(requireContext())
                alertDialog.setTitle("Index")
                alertDialog.setSingleChoiceItems(list,indexPosition
                ) { _, p1 -> indexPosition = p1 }
                alertDialog.setNegativeButton("Cancel"
                ) { _, _ -> }.setPositiveButton("OK"){_,_->
                    if(indexPosition==0){
                        movieButtonClicked()
                        binding.drawableLayout.closeDrawer(GravityCompat.START)
                    }else{
                        tvShowButtonClicked()
                        binding.drawableLayout.closeDrawer(GravityCompat.START)
                    }
                }.show()
            }
            R.id.movie_filter->{

                val alertDialog=AlertDialog.Builder(requireContext())
                if(isItInMovieList==true){
                   alertDialog.setMultiChoiceItems(
                        movie_genre_list, movie_booleanArray
                    ) { _, p1, p2 -> movie_booleanArray[p1] = p2 }

                    alertDialog.setTitle("Genre").setNegativeButton("CANCEL"
                    ) { _, _ -> }.setPositiveButton("OK") { _, _ ->
                        movieGenres = ""
                        movie_booleanArray.forEachIndexed { index, _ ->
                            if (movie_booleanArray[index]) {
                                val value = movie_genre_list[index]
                                movieGenres = if (movieGenres.isEmpty()) {
                                    movieGenreMap[value].toString()
                                } else {
                                    movieGenres+ "," + movieGenreMap[value].toString()
                                }
                            }
                        }
                        movieButtonClicked()
                        binding.drawableLayout.closeDrawer(GravityCompat.START)
                    }.show()
                }else{
                    alertDialog.setMultiChoiceItems(
                        tvshow_genre_list, tvshow_booleanArray
                    ) { _, p1, p2 -> tvshow_booleanArray[p1] = p2 }

                    alertDialog.setTitle("Genre").setNegativeButton("CANCEL"
                    ) { _, _ -> }.setPositiveButton("OK") { _, _ ->
                        tvShowGenres= ""
                        tvshow_booleanArray.forEachIndexed { index, _ ->
                            if (tvshow_booleanArray[index]) {
                                val value = tvshow_genre_list[index]
                                tvShowGenres = if (tvShowGenres.isEmpty()) {
                                    tvShowGenreMap[value].toString()
                                } else {
                                    tvShowGenres + "," + tvShowGenreMap[value].toString()
                                }
                            }
                        }
                        tvShowButtonClicked()
                        binding.drawableLayout.closeDrawer(GravityCompat.START)
                    }.show()
                }


            }
            R.id.sort->{
                val alertDialog=AlertDialog.Builder(requireContext())
                if(isItInMovieList==true){
                    alertDialog.setTitle("Sort By").setSingleChoiceItems(
                        tvShowSortArray,movieSortPosition
                    ) { _, p1 -> movieSortPosition = p1 }.setNegativeButton("CANCEL"
                    ) { _, _ -> }.setPositiveButton("OK") { _, _ ->
                            when(movieSortPosition){
                                0->movieSortString="popularity.desc"
                                1->movieSortString="first_air_date.desc"
                                2->movieSortString="vote_average.desc"
                            }
                        movieButtonClicked()
                    }.show()
                }else{
                    alertDialog.setTitle("Sort By").setSingleChoiceItems(
                      tvShowSortArray,tvShowSortPosition
                    ) { _, p1 -> tvShowSortPosition = p1 }.setNegativeButton("CANCEL"
                    ) { _, _ -> }.setPositiveButton("OK") { _, _ ->
                        when(tvShowSortPosition){
                            0->tvShowSortString="popularity.desc"
                            1->tvShowSortString="release_date.desc"
                            2->tvShowSortString="vote_average.desc"
                        }
                        tvShowButtonClicked()
                    }.show()
                }

            }
            R.id.quality->{
                val alertDialog=AlertDialog.Builder(requireContext())
                alertDialog.setTitle("Quality").setMultiChoiceItems(qualityArray, booleanArray2
                ) { _, p1, p2 -> booleanArray2[p1] = p2 }.setNegativeButton("CANCEL"
                ) { _, _ ->
                    booleanArray2.forEachIndexed { index, _->
                        booleanArray2[index] = false
                    }
                }.setPositiveButton("OK") { _, _ ->
                    //search for quality
                }.show()
            }
            R.id.like->{
                binding.drawableLayout.closeDrawer(GravityCompat.START)
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToWatchListFragment())
            }
            else ->{
                Toast.makeText(requireContext(),"Error",Toast.LENGTH_SHORT).show()
            }
        }
    }

}