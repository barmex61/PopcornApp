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
import com.fatih.popcornapplication.databinding.MovieRecyclerviewRowBinding
import com.fatih.popcornapplication.model.ResultMovies
import com.fatih.popcornapplication.view.MainFragmentDirections
import javax.inject.Inject


class MovieAdapter @Inject constructor(): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private val diffUtil=object :DiffUtil.ItemCallback<ResultMovies>(){
        override fun areItemsTheSame(oldItem: ResultMovies, newItem: ResultMovies): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: ResultMovies, newItem: ResultMovies): Boolean {
            return oldItem==newItem
        }

    }
    private val listDiffer=AsyncListDiffer(this,diffUtil)

    var movieList:List<ResultMovies>
        get() = listDiffer.currentList
        set(value) = listDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding=DataBindingUtil.inflate<MovieRecyclerviewRowBinding>(LayoutInflater.from(parent.context),
            R.layout.movie_recyclerview_row,parent,false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.binding.result=movieList[position]
        if(movieList[position].releaseDate.isNotEmpty()&&movieList[position].releaseDate.substring(0,4).isNotEmpty()){
            holder.binding.releaseDate=movieList[position].releaseDate.substring(0,4)
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
                val vibrantColor=it!!.getVibrantColor(ContextCompat.getColor(holder.itemView.context,R.color.white))
                val action=MainFragmentDirections.actionMainFragmentToDetailsFragment(movieList[position].id,vibrantColor,false)
                Navigation.findNavController(view).navigate(action)
            }
        }
    }

}