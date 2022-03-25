package com.fatih.popcornapplication.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatih.popcornapplication.R
import com.fatih.popcornapplication.adapter.SeasonAdapter
import com.fatih.popcornapplication.databinding.FragmentDetailsBinding
import com.fatih.popcornapplication.databinding.SeasonsBottomSheetDialogBinding
import com.fatih.popcornapplication.model.*
import com.fatih.popcornapplication.resource.Status
import com.fatih.popcornapplication.viewModel.DetailsFragmentViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment @Inject constructor( private var seasonAdapter:SeasonAdapter) : Fragment() {

    lateinit var viewModel:DetailsFragmentViewModel
    private lateinit var binding:FragmentDetailsBinding
    var selectedMovieId:Int?=null
    var selectedTvShowId:Int?=null
    private var selectedTvShow:TvShowDetail?=null
    private var selectedMovie:MovieDetail?=null
    private var url:String?=null
    private var vibrantColor:Int?=null
    private var movieImageList=ArrayList<MoviePoster>()
    private var tvShowImageList=ArrayList<TvShowPoster>()
    private val colorMatrix=ColorMatrix()
    private var _isTvShow:Boolean?=null
    private var episodesBottomSheetDialog: BottomSheetDialog?=null
    private lateinit var episodesBottomSheetDialogBinding: SeasonsBottomSheetDialogBinding
    private lateinit var colorMatrixColorFilter: ColorMatrixColorFilter
    private var isItInWatchList:Boolean?=false
    lateinit var roomEntity: RoomEntity
    private lateinit var button_animation:Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        colorMatrix.setSaturation(0f)
        colorMatrixColorFilter= ColorMatrixColorFilter(colorMatrix)


    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_details,container,false)
        doInitialization()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, _, _, _ -> binding.layoutHeader.y= binding.nestedScrollView.scrollY.toFloat()-binding.nestedScrollView.scrollY.toFloat()/2.6f })
        val callback=object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun doInitialization(){
        viewModel=ViewModelProvider(this)[DetailsFragmentViewModel::class.java]
        button_animation=AnimationUtils.loadAnimation(requireContext(),R.anim.button_animation)
        binding.trailerImage.setOnClickListener { youtube() }
        binding.watchList.setOnClickListener { watchList() }
        binding.shareButton.setOnClickListener { findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToWatchListFragment()) }
        binding.reviewImage.setOnClickListener { goWeb() }
        binding.backButton.setOnClickListener { findNavController().popBackStack() }
        binding.episodesImage.setOnClickListener {view-> goEpisodes(view) }
        binding.backgroundImage.colorFilter = colorMatrixColorFilter
        arguments?.let {
            _isTvShow=DetailsFragmentArgs.fromBundle(it).isTvShow
            if(_isTvShow!!){
                selectedTvShowId=DetailsFragmentArgs.fromBundle(it).id
                isItInDatabase(selectedTvShowId!!)
                selectedMovieId=null
            }else{
                selectedMovieId=DetailsFragmentArgs.fromBundle(it).id
                isItInDatabase(selectedMovieId!!)
                selectedTvShowId=null
            }

            vibrantColor=DetailsFragmentArgs.fromBundle(it).vibrantColor
        }
        observeMovieLiveData()


    }

    private fun observeMovieLiveData(){
        binding.isLoading=true
        selectedMovieId?.let { selectedId->
            viewModel.getMovieDetails(selectedId)
            viewModel.movieDetails.observe(viewLifecycleOwner){ resource->
                if(resource!=null){
                    when (resource.status) {
                        Status.LOADING -> {
                            binding.imgPlay.visibility=View.INVISIBLE
                            binding.saveImage.visibility=View.INVISIBLE
                            binding.trailerImage.visibility=View.INVISIBLE
                        }
                        Status.ERROR -> {
                            binding.mainLayout.visibility=View.INVISIBLE
                            binding.isLoading=false
                            Toast.makeText(requireContext(),"Error",Toast.LENGTH_SHORT).show()
                        }
                        Status.SUCCESS -> {
                            resource.data?.let {
                                selectedMovie=it
                                roomEntity=
                                    RoomEntity(it.releaseDate,it.posterPath,it.voteAverage,false,it.id)
                                setTints(it,null)
                                url=it.homepage
                                binding.isLoading=false
                                binding.imgPlay.visibility=View.VISIBLE
                                binding.saveImage.visibility=View.VISIBLE
                                binding.trailerImage.visibility=View.VISIBLE

                            }
                        }
                    }
                }

            }
            viewModel.getMovieImages(selectedId)
            viewModel.movieImages.observe(viewLifecycleOwner){ resource->
                if(resource!=null){
                    when(resource.status){

                        Status.LOADING->{
                            binding.imgPlay.visibility=View.INVISIBLE
                            binding.saveImage.visibility=View.INVISIBLE
                            binding.trailerImage.visibility=View.INVISIBLE
                        }
                        Status.ERROR->{
                            binding.mainLayout.visibility=View.INVISIBLE
                            binding.isLoading=false
                            Toast.makeText(requireContext(),"Error",Toast.LENGTH_SHORT).show()
                        }
                        Status.SUCCESS->{
                            resource.data?.let {
                                val oldSize=movieImageList.size
                                binding.isLoading=false
                                binding.imgPlay.visibility=View.VISIBLE
                                binding.saveImage.visibility=View.VISIBLE
                                binding.trailerImage.visibility=View.VISIBLE

                                movieImageList.addAll(it.moviePosters)
                                if(oldSize!=movieImageList.size){
                                    getImagesFromPicasso(movieImageList,null)
                                }
                            }
                        }
                    }
                }

            }
        }
        selectedTvShowId?.let {
            viewModel.getTvShowDetails(it)
            viewModel.tvShowDetails.observe(viewLifecycleOwner){resource->
                if(resource!=null){
                    when (resource.status) {
                        Status.LOADING -> {
                            binding.imgPlay.visibility=View.INVISIBLE
                            binding.saveImage.visibility=View.INVISIBLE
                            binding.trailerImage.visibility=View.INVISIBLE
                        }
                        Status.ERROR -> {
                            binding.mainLayout.visibility=View.INVISIBLE
                            binding.isLoading=false
                            Toast.makeText(requireContext(),"Error",Toast.LENGTH_SHORT).show()
                        }
                        Status.SUCCESS -> {

                            resource.data?.let { it->
                                selectedTvShow=it
                                roomEntity= RoomEntity(it.lastAirDate,it.posterPath,it.voteAverage,true,it.id)
                                url=it.homepage
                                setTints(null,it)
                                seasonAdapter.listTvShowSeason=it.tvShowSeasons
                                seasonAdapter.rating=binding.ratingText.text
                                seasonAdapter.genre=binding.genreText.text
                                binding.isLoading=false
                                binding.imgPlay.visibility=View.VISIBLE
                                binding.saveImage.visibility=View.VISIBLE
                                binding.trailerImage.visibility=View.VISIBLE
                            }
                        }
                    }
                }
            }
            viewModel.getTvShowImages(it)
            viewModel.tvShowImages.observe(viewLifecycleOwner){resource->

                if(resource!=null){
                    when(resource.status){
                        Status.LOADING->{
                            binding.imgPlay.visibility=View.INVISIBLE
                            binding.saveImage.visibility=View.INVISIBLE
                            binding.trailerImage.visibility=View.INVISIBLE
                        }
                        Status.ERROR->{
                            binding.mainLayout.visibility=View.INVISIBLE
                            binding.isLoading=false
                            Toast.makeText(requireContext(),"Error",Toast.LENGTH_SHORT).show()
                        }
                        Status.SUCCESS->{
                            resource.data?.let {it->
                                val oldSize=tvShowImageList.size
                                binding.isLoading=false
                                binding.imgPlay.visibility=View.VISIBLE
                                binding.saveImage.visibility=View.VISIBLE
                                binding.trailerImage.visibility=View.VISIBLE
                                tvShowImageList.addAll(it.tvShowPosters)
                                if(oldSize!=tvShowImageList.size){
                                    getImagesFromPicasso(null,tvShowImageList)
                                }
                            }
                        }
                    }
                }
            }

        }

    }
    private fun isItInDatabase(id:Int){
        viewModel.isItInDatabase(id)
            viewModel.roomEntity.observe(viewLifecycleOwner){resource->
                isItInWatchList = resource!=null
                if(isItInWatchList==true){
                    binding.watchList.imageTintList= ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.white))
                }else{
                    binding.watchList.imageTintList=ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.gray))
                }
        }
    }

    private fun setTints(selectedMovie:MovieDetail?, selectedTvShow:TvShowDetail?){
        vibrantColor?.let {
            viewModel.setTints(selectedMovie,selectedTvShow,binding,requireContext(),it)
        }
    }
    private fun getImagesFromPicasso(movieImages:ArrayList<MoviePoster>?,tvShowImages:ArrayList<TvShowPoster>?) {
            viewModel.getImagesFromPicasso(movieImages,tvShowImages,binding)
    }
    private fun goEpisodes(view:View){
        if(episodesBottomSheetDialog==null){

            episodesBottomSheetDialog= BottomSheetDialog(requireContext(),R.style.BottomSheetDialog)
            episodesBottomSheetDialogBinding=DataBindingUtil.inflate(LayoutInflater.from(requireContext()),R.layout.seasons_bottom_sheet_dialog,view.findViewById(R.id.episodesContainer),false)
            episodesBottomSheetDialog?.setContentView(episodesBottomSheetDialogBinding.root)
            episodesBottomSheetDialog?.dismissWithAnimation=true
            episodesBottomSheetDialogBinding.episodesRecyclerView.adapter=seasonAdapter
            episodesBottomSheetDialogBinding.layoutHeader.setBackgroundColor(vibrantColor!!)
            episodesBottomSheetDialogBinding.episodesRecyclerView.layoutManager=LinearLayoutManager(requireContext())
            episodesBottomSheetDialogBinding.closeImage.setOnClickListener { episodesBottomSheetDialog?.dismiss() }
        }
        episodesBottomSheetDialog?.show()
    }
    private fun goWeb(){
        val intent=Intent(Intent.ACTION_VIEW)
        url?.let {
            intent.data= Uri.parse(it)
        }
        startActivity(intent)
    }
    private fun watchList(){
        if(isItInWatchList==false){
            binding.watchList.imageTintList= ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.white))
            roomEntity.let { roomEntity->
                viewModel.addTvShowIntoDatabase(roomEntity)
            }
            if(_isTvShow==true){
                Toast.makeText(requireContext(),"Tv show added into Watchlist",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),"Movie added into Watchlist",Toast.LENGTH_SHORT).show()
            }
            isItInWatchList=true
        }else{
            binding.watchList.imageTintList= ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.gray))
            roomEntity.let { roomEntity ->
                viewModel.deleteTvShowFromDatabase(roomEntity)
            }
            if(_isTvShow==true){
                Toast.makeText(requireContext(),"Tv show deleted from Watchlist",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),"Movie deleted from Watchlist",Toast.LENGTH_SHORT).show()
            }
            isItInWatchList=false
        }
    }
    private fun youtube(){
        var id=if(selectedMovieId==null){
            selectedTvShowId
        }else{
            selectedMovieId
        }
        findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToMoviePlayFragment(id!!))
    }

}