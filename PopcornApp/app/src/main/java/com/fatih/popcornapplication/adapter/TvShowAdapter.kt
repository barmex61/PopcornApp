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
import com.fatih.popcornapplication.databinding.TvshowRecyclerviewRowBinding
import com.fatih.popcornapplication.model.ResultTvShow
import com.fatih.popcornapplication.view.MainFragmentDirections
import javax.inject.Inject

class TvShowAdapter @Inject constructor(): RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder>() {

    private val diffUtil=object :DiffUtil.ItemCallback<ResultTvShow>(){
        override fun areItemsTheSame(oldItem: ResultTvShow, newItem: ResultTvShow): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: ResultTvShow, newItem: ResultTvShow): Boolean {
            return oldItem==newItem
        }
    }
    private val listDiffer=AsyncListDiffer(this,diffUtil)

    var tvShowList:List<ResultTvShow>
        get() = listDiffer.currentList
        set(value) = listDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowViewHolder {
        val binding=DataBindingUtil.inflate<TvshowRecyclerviewRowBinding>(LayoutInflater.from(parent.context),
            R.layout.tvshow_recyclerview_row,parent,false)
        return TvShowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
            holder.binding.result=tvShowList[position]
            if(tvShowList[position].firstAirDate.isNotEmpty()&&tvShowList[position].firstAirDate.substring(0,4).isNotEmpty()){
            holder.binding.releaseDate=tvShowList[position].firstAirDate.substring(0,4)
            }else{
            holder.binding.releaseDate="null"
            }
            holder.itemView.setOnClickListener {
               getDominantColor(holder,it,position)
            }
    }

    override fun getItemCount(): Int {
        return tvShowList.size
        }
    class TvShowViewHolder(val binding:TvshowRecyclerviewRowBinding) :RecyclerView.ViewHolder(binding.root)

    private fun getDominantColor(holder: TvShowViewHolder, view: View, position: Int){
        if(holder.binding.movieImage.drawable!=null){
            val drawable: BitmapDrawable = holder.binding.movieImage.drawable as BitmapDrawable
            val bitmap: Bitmap =drawable.bitmap
            Palette.Builder(bitmap).generate {
                var vibrantColor=it!!.getVibrantColor(ContextCompat.getColor(holder.itemView.context,R.color.white))
                val action=MainFragmentDirections.actionMainFragmentToDetailsFragment(tvShowList[position].id,vibrantColor,true)
                Navigation.findNavController(view).navigate(action)
            }
        }
    }

}