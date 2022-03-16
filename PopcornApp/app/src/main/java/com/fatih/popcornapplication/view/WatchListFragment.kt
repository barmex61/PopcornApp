package com.fatih.popcornapplication.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.fatih.popcornapplication.adapter.WatchListAdapter
import com.fatih.popcornapplication.viewModel.WatchListFragmentViewModel
import com.fatih.popcornapplication.R
import com.fatih.popcornapplication.databinding.FragmentWatchListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WatchListFragment @Inject constructor(private val watchListAdapter: WatchListAdapter): Fragment() {

    private lateinit var viewModel:WatchListFragmentViewModel
    private lateinit var binding: FragmentWatchListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_watch_list,container,false)
        doInitialization()
        return binding.root
    }

    private fun doInitialization(){
        viewModel= ViewModelProvider(this)[WatchListFragmentViewModel::class.java]
        observeLiveData()
        binding.watchListRecyclerView.layoutManager=GridLayoutManager(requireContext(),3)
        //viewModel.setBackgroundColor(requireContext(),binding)
        binding.backImage.setOnClickListener { findNavController().popBackStack() }
    }
    private fun observeLiveData(){
        binding.isLoading=true

            viewModel.watchList.observe(viewLifecycleOwner){resource->

                binding.watchListRecyclerView.visibility=View.VISIBLE
                binding.isLoading=false
                resource.let {
                    watchListAdapter.watch=it
                    binding.watchListRecyclerView.adapter=watchListAdapter
                }
            }
    }
}
