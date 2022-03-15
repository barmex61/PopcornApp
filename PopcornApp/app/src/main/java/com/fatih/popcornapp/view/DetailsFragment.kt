package com.fatih.popcornapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatih.popcornapp.R
import com.fatih.popcornapp.adapter.SeasonAdapter
import com.fatih.popcornapp.databinding.FragmentDetailsBinding
import com.fatih.popcornapp.databinding.SeasonsBottomSheetDialogBinding
import com.fatih.popcornapp.model.*
import com.fatih.popcornapp.resource.Status
import com.fatih.popcornapp.viewModel.DetailsFragmentViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private lateinit var viewModel:DetailsFragmentViewModel
    private lateinit var binding:FragmentDetailsBinding
    private var selectedMovieId:Int?=null
    private var selectedTvShowId:Int?=null
    private var selectedTvShow:TvShowDetail?=null
    private var selectedMovie:MovieDetail?=null
    private var url:String?=null
    private var listTvShowSeason=ArrayList<TvShowSeason>()
    private var vibrantColor:Int?=null
    private var movieImageList=ArrayList<MoviePoster>()
    private var tvShowImageList=ArrayList<TvShowPoster>()
    private val colorMatrix=ColorMatrix()
    private var _isTvShow:Boolean?=null
    private lateinit var seasonAdapter:SeasonAdapter
    private var episodesBottomSheetDialog: BottomSheetDialog?=null
    private lateinit var episodesBottomSheetDialogBinding: SeasonsBottomSheetDialogBinding
    private lateinit var colorMatrixColorFilter: ColorMatrixColorFilter
    private var isItInWatchList:Boolean?=false
    private lateinit var roomEntity: RoomEntity
    private var counter=0
    private var videoId:String?=null

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
    }
    private fun doInitialization(){
        viewModel=ViewModelProvider(this)[DetailsFragmentViewModel::class.java]
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
            viewModel.getVideos("movie",selectedId)
            viewModel.videos.observe(viewLifecycleOwner){resource->
                if(resource!=null){
                    when(resource.status){
                        Status.SUCCESS->{
                            resource.data?.let {
                                videoId=it.videoResults[0].key
                            }
                        }else ->{
                            binding.isLoading=true
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
                                roomEntity=
                                    RoomEntity(it.lastAirDate,it.posterPath,it.voteAverage,true,it.id)
                                url=it.homepage
                                setTints(null,it)
                                val oldCount=listTvShowSeason.size
                                listTvShowSeason.addAll(it.tvShowSeasons)
                                if(oldCount!=listTvShowSeason.size){
                                    seasonAdapter= SeasonAdapter(listTvShowSeason,binding.ratingText.text,binding.genreText.text)
                                }
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
    @SuppressLint("SetTextI18n")
    private fun setTints(selectedMovie:MovieDetail?, selectedTvShow:TvShowDetail?){
        binding.color=vibrantColor

        selectedMovie?.let {it->
            binding.genreText.text= it.movieGenres[0].name
            binding.ratingBar.rating= it.voteAverage.toFloat()
            binding.textDescription.text=it.overview
            binding.runtimeText.text=it.runtime.toString()+" min"
            binding.imageUrl=it.backdropPath
            binding.ratingText.text=it.voteAverage.toString()
            binding.nameText.text=it.originalTitle
            binding.yearText.text=it.releaseDate
            binding.episodesImage.visibility=View.INVISIBLE
            binding.episodesText.visibility=View.INVISIBLE
        }
        selectedTvShow?.let { it->
            binding.genreText.text= it.genres[0].name
            binding.ratingBar.rating= it.voteAverage.toFloat()
            binding.textDescription.text=it.overview
            binding.runtimeText.text=it.episodeRunTime[0].toString()+" min"
            binding.imageUrl=it.backdropPath
            binding.ratingText.text=it.voteAverage.toString()
            binding.nameText.text=it.name
            binding.yearText.text=it.lastAirDate
            binding.episodesImage.visibility=View.VISIBLE
            binding.episodesText.visibility=View.VISIBLE
        }

        binding.imgPlay.imageTintList=ColorStateList.valueOf(vibrantColor!!)
        binding.saveImage.backgroundTintList= ColorStateList.valueOf(vibrantColor!!)
        binding.trailerImage.imageTintList= ColorStateList.valueOf(vibrantColor!!)
        binding.ratingBar.progressTintList= ColorStateList.valueOf(vibrantColor!!)
        binding.ratingBar.backgroundTintList=ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.white))
        binding.ratingBar.progressBackgroundTintList=ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.white))
    }
    private fun getImagesFromPicasso(movieImages:ArrayList<MoviePoster>?,tvShowImages:ArrayList<TvShowPoster>?) {
       movieImages?.let { it->
           var x=10
           if(it.size<10){
               x=it.size
           }
           CoroutineScope(Dispatchers.IO).launch {
               for(i in 0 until x){
                   val url=it[i].filePath
                   try {
                       withContext(Dispatchers.Main){
                           binding.backgroundImage.alpha=0f
                           Picasso.get().load("https://www.themoviedb.org/t/p/w600_and_h900_bestv2$url").into(binding.backgroundImage,object:Callback{
                               override fun onSuccess() {
                                   binding.backgroundImage.animate().alpha(1f).setDuration(3000).withEndAction {
                                       binding.backgroundImage.animate().alpha(0f).setDuration(2500).start()
                                   }.start()
                               }

                               override fun onError(e: java.lang.Exception?) {
                                   println(e!!.message)
                               }

                           })
                       }

                   }catch (e:Exception){
                       println(e)
                   }
                   delay(5500)
               }

           }
       }
        tvShowImages?.let {it->
            var x=10
            if(it.size<10){
                x=it.size
            }
            CoroutineScope(Dispatchers.IO).launch {
                for(i in 0 until x){
                    val url=it[i].filePath
                    try {
                        withContext(Dispatchers.Main){
                            binding.backgroundImage.alpha=0f
                            Picasso.get().load("https://www.themoviedb.org/t/p/w600_and_h900_bestv2$url").into(binding.backgroundImage,object:Callback{
                                override fun onSuccess() {
                                    binding.backgroundImage.animate().alpha(1f).setDuration(3000).withEndAction {
                                        binding.backgroundImage.animate().alpha(0f).setDuration(2500).start()
                                    }.start()
                                }

                                override fun onError(e: java.lang.Exception?) {
                                    println(e!!.message)
                                }

                            })
                        }

                    }catch (e:Exception){
                        println(e)
                    }
                    delay(5500)
                }

            }
        }
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

        if(counter%2==0){
            binding.videoImage.setImageResource(R.drawable.ic_image)
            binding.trailerText.text="IMAGES"
            binding.playArrow.visibility=View.GONE
            binding.imgPlay.visibility=View.GONE
            binding.saveImage.visibility=View.GONE
            binding.posterImage.visibility=View.GONE
            binding.youtuber.visibility=View.VISIBLE
            lifecycle.addObserver(binding.youtuber)
            binding.youtuber.enableAutomaticInitialization=true
            binding.youtuber.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    videoId?.let {
                        youTubePlayer.loadVideo(it,0f)
                    }

                }
            })

        }else{
            binding.youtuber.release()
            binding.youtuber.visibility=View.GONE
            binding.videoImage.setImageResource(R.drawable.ic_video)
            binding.trailerText.text="TRAILER"
            binding.playArrow.visibility=View.VISIBLE
            binding.imgPlay.visibility=View.VISIBLE
            binding.saveImage.visibility=View.VISIBLE
            binding.posterImage.visibility=View.VISIBLE

        }
        counter++
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.youtuber.release()
    }
}