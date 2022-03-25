package com.fatih.popcornapplication.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatih.popcornapplication.R
import com.fatih.popcornapplication.adapter.YoutubeVideoAdapter
import com.fatih.popcornapplication.databinding.FragmentMoviePlayBinding
import com.fatih.popcornapplication.model.İtem
import com.fatih.popcornapplication.resource.Status
import com.fatih.popcornapplication.viewModel.TrailerFragmentViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TrailerFragment @Inject constructor(private val youtubeVideoAdapter: YoutubeVideoAdapter): Fragment(R.layout.fragment_movie_play) {

    private lateinit var binding:FragmentMoviePlayBinding
    private lateinit var viewModel:TrailerFragmentViewModel
    private var myVideoId:String?=""
    private var selectedId=0
    private var videoUrlArrayList=ArrayList<String>()
    private var itemList=ArrayList<İtem>()
    private var part="snippet,contentDetails,statistics"
    private var position=0
    private var listener:AbstractYouTubePlayerListener?=null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding= FragmentMoviePlayBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        doInitialization()

    }
    private fun doInitialization(){
        binding.backButton.setOnClickListener { findNavController().popBackStack() }
        viewModel=ViewModelProvider(requireActivity())[TrailerFragmentViewModel::class.java]
        binding.youtubeRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.youtubeRecyclerView.adapter=youtubeVideoAdapter
        arguments?.let {
            selectedId=TrailerFragmentArgs.fromBundle(it).id
        }
        observeLiveData()
        lifecycle.addObserver(binding.youtubePlayer)
        listener=object:AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.cueVideo(videoUrlArrayList[position],0f)
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                super.onStateChange(youTubePlayer, state)
                println("state")
            }

            override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
                super.onVideoId(youTubePlayer, videoId)
                println("videoıd")
            }

        }

        binding.youtubePlayer.addYouTubePlayerListener(listener as AbstractYouTubePlayerListener)
        youtubeVideoAdapter.setOnItemClickListener {
            position=it
            binding.youtubePlayer.getYouTubePlayerWhenReady(object :YouTubePlayerCallback{
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(videoUrlArrayList[position],0f)
                }
            })

        }

    }
    @SuppressLint("NotifyDataSetChanged")
    private fun observeLiveData(){
        viewModel.getVideos("movie",selectedId)
        viewModel.videos.observe(viewLifecycleOwner){resource->
            if(resource!=null){
                when(resource.status){
                    Status.SUCCESS->{
                        resource.data?.let {
                            it.videoResults.map {it->
                                if(it.site=="YouTube"){
                                    videoUrlArrayList.add(it.key)
                                    myVideoId = if(myVideoId?.isEmpty()!!){
                                        it.key
                                    }else{
                                        myVideoId+","+it.key
                                    }
                                }
                            }
                            myVideoId?.let {
                                viewModel.getYoutubeVideos(it,part)
                            }
                        }
                    }else->{

                    }
                }
            }
        }

        viewModel.youtubeVideos.observe(viewLifecycleOwner){resources->
            if(resources!=null){
                when(resources.status){
                    Status.ERROR->{
                        binding.isLoading=false
                        Toast.makeText(requireContext(),resources.message,Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING->{
                        binding.isLoading=true

                    }
                    Status.SUCCESS->{
                        binding.isLoading=false
                        resources.data?.let { it->

                            itemList=ArrayList(it.items)
                            youtubeVideoAdapter.youtubeList=itemList
                            youtubeVideoAdapter.notifyDataSetChanged()
                        }
                    }

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoUrlArrayList.clear()
    }
}