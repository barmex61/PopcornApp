package com.fatih.popcornapp.adapter

import android.annotation.SuppressLint
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
import com.fatih.popcornapp.databinding.MovieRecyclerviewRowBinding
import com.fatih.popcornapp.model.ResultMovies
import com.fatih.popcornapp.view.MainFragmentDirections


class MovieAdapter(var movieList:ArrayList<ResultMovies>): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding=DataBindingUtil.inflate<MovieRecyclerviewRowBinding>(LayoutInflater.from(parent.context),
            R.layout.movie_recyclerview_row,parent,false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.binding.result=movieList[position]
        if(movieList[position].releaseDate.isNotEmpty()&&movieList[position].releaseDate.substring(0,4).isNotEmpty()){
            holder.binding.releaseDate=movieList[position].releaseDate.substring(0,4)
        }else{
            holder.binding.releaseDate="null"
        }
        holder.itemView.setOnClickListener {
            getDominantColor(holder,it,position)
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
    class MovieViewHolder(val binding: MovieRecyclerviewRowBinding) :RecyclerView.ViewHolder(binding.root)

    private fun getDominantColor(holder:MovieViewHolder,view: View,position: Int){
        if(holder.binding.movieImage.drawable!=null){
            val drawable: BitmapDrawable = holder.binding.movieImage.drawable as BitmapDrawable
            val bitmap: Bitmap =drawable.bitmap
            Palette.Builder(bitmap).generate {
                var vibrantColor=it!!.getVibrantColor(ContextCompat.getColor(holder.itemView.context,R.color.white))
                val action=MainFragmentDirections.actionMainFragmentToDetailsFragment(movieList[position].id,vibrantColor,false)
                Navigation.findNavController(view).navigate(action)
            }
        }
    }

}