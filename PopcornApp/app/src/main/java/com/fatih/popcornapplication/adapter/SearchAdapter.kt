package com.fatih.popcornapplication.adapter

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fatih.popcornapplication.R
import com.fatih.popcornapplication.databinding.SearchRecyclerRowBinding
import com.fatih.popcornapplication.model.SearchResult
import com.fatih.popcornapplication.view.MainFragmentDirections
import javax.inject.Inject

class SearchAdapter @Inject constructor(): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    private val diffUtil=object :DiffUtil.ItemCallback<SearchResult>(){
        override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
            return oldItem==newItem
        }
    }
    private val listDiffer=AsyncListDiffer(this,diffUtil)

    var searchList: List<SearchResult>
        get() = listDiffer.currentList
        set(value) = listDiffer.submitList(value)

    private var isTvShow:Boolean?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
            val binding=DataBindingUtil.inflate<SearchRecyclerRowBinding>(LayoutInflater.from(parent.context),
                R.layout.search_recycler_row,parent,false)
        return SearchViewHolder(binding)
        }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
            holder.binding.result=searchList[position]
            holder.binding.releaseDate=searchList[position].releaseDate
            holder.itemView.setOnClickListener {
                getDominantColor(holder,it,position)
            }
    }

    override fun getItemCount(): Int {
            return searchList.size
        }
    class SearchViewHolder( val binding:SearchRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)

    private fun getDominantColor(holder: SearchViewHolder, view: View, position: Int){
        if(holder.binding.movieImage.drawable!=null){
            val drawable: BitmapDrawable = holder.binding.movieImage.drawable as BitmapDrawable
            val bitmap: Bitmap =drawable.bitmap
            Palette.Builder(bitmap).generate {
                val vibrantColor=it!!.getVibrantColor(ContextCompat.getColor(holder.itemView.context,R.color.white))
                val action=MainFragmentDirections.actionMainFragmentToDetailsFragment(searchList[position].id,vibrantColor,isTvShow!!)
                Navigation.findNavController(view).navigate(action)
            }
        }
    }
    fun updateInfo(isTvShow:Boolean){
        this.isTvShow=!isTvShow
    }
}