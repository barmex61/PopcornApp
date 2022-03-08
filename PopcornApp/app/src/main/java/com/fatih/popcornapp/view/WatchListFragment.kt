package com.fatih.popcornapp.view

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.fatih.popcornapp.R
import com.fatih.popcornapp.adapter.WatchListAdapter
import com.fatih.popcornapp.databinding.FragmentWatchListBinding
import com.fatih.popcornapp.resource.Status
import com.fatih.popcornapp.viewModel.WatchListFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WatchListFragment : Fragment() {

    private lateinit var viewModel:WatchListFragmentViewModel
    private lateinit var binding:FragmentWatchListBinding
    private lateinit var adapter:WatchListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_watch_list,container,false)
        doInitialization()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    private fun doInitialization(){
        viewModel= ViewModelProvider(this)[WatchListFragmentViewModel::class.java]
        observeLiveData()
        binding.watchListRecyclerView.layoutManager=GridLayoutManager(requireContext(),3)
        setBackgroundColor()
        binding.backImage.setOnClickListener { findNavController().popBackStack() }
    }
    private fun observeLiveData(){
        binding.isLoading=true
        viewModel.getAllWatchListData().observe(viewLifecycleOwner){resource->

            if(resource!=null){
                when(resource.status){
                    Status.LOADING->{
                        binding.watchListRecyclerView.visibility=View.GONE
                        binding.isLoading=true
                        println("loading")
                    }
                    Status.ERROR->{
                        binding.watchListRecyclerView.visibility=View.GONE
                        binding.isLoading=false
                        println("error")
                    }
                    Status.SUCCESS->{
                        binding.watchListRecyclerView.visibility=View.VISIBLE
                        binding.isLoading=false
                        resource.data?.let {
                            adapter= WatchListAdapter(ArrayList(it))
                            binding.watchListRecyclerView.adapter=adapter
                        }
                    }
                }
            }

        }
    }

    private fun setBackgroundColor(){
        val color=ArrayList<Int>()
        color.add(ContextCompat.getColor(requireContext(),R.color.black2))
        color.add(ContextCompat.getColor(requireContext(),R.color.red))
        color.add(ContextCompat.getColor(requireContext(),R.color.nown))
        color.add(ContextCompat.getColor(requireContext(),R.color.opakwhite))
        color.add(ContextCompat.getColor(requireContext(),R.color.green))
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
        }
    }
