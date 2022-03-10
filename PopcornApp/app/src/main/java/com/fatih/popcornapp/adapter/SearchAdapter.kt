package com.fatih.popcornapp.adapter

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.fatih.popcornapp.R
import com.fatih.popcornapp.databinding.SearchRecyclerRowBinding
import com.fatih.popcornapp.model.SearchResult
import com.fatih.popcornapp.view.MainFragmentDirections

class SearchAdapter(private var searchList:ArrayList<SearchResult>): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
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
                var vibrantColor=it!!.getVibrantColor(ContextCompat.getColor(holder.itemView.context,R.color.white))
                val action=MainFragmentDirections.actionMainFragmentToDetailsFragment(searchList[position].id,vibrantColor,isTvShow!!)
                Navigation.findNavController(view).navigate(action)
            }
        }
    }
    fun updateInfo(isTvShow:Boolean){
        this.isTvShow=!isTvShow
    }
}