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
import com.fatih.popcornapplication.databinding.WatchListLayoutRowBinding
import com.fatih.popcornapplication.model.RoomEntity
import com.fatih.popcornapplication.view.WatchListFragmentDirections
import javax.inject.Inject


class WatchListAdapter @Inject constructor(): RecyclerView.Adapter<WatchListAdapter.WatchListViewHolder>() {

    private val diffUtil=object :DiffUtil.ItemCallback<RoomEntity>(){
        override fun areItemsTheSame(oldItem: RoomEntity, newItem: RoomEntity): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: RoomEntity, newItem: RoomEntity): Boolean {
            return oldItem==newItem
        }
    }
    private val listDiffer=AsyncListDiffer(this,diffUtil)
    var watch:List<RoomEntity>
        get() = listDiffer.currentList
        set(value) = listDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchListViewHolder {
        val binding=DataBindingUtil.inflate<WatchListLayoutRowBinding>(LayoutInflater.from(parent.context),
            R.layout.watch_list_layout_row,parent,false)
        return WatchListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchListViewHolder, position: Int) {
        holder.binding.watchList=watch[position]
        if(watch[position].lastAirDate.isNotEmpty()&&watch[position].lastAirDate.substring(0,4).isNotEmpty()){
            holder.binding.releaseDate.text=watch[position].lastAirDate.substring(0,4)
        }else{
            holder.binding.releaseDate.text="null"
        }
        holder.itemView.setOnClickListener {
            getDominantColor(holder,it,position)
        }
    }

    override fun getItemCount(): Int {
        return watch.size
    }
    class WatchListViewHolder(val binding:WatchListLayoutRowBinding) : RecyclerView.ViewHolder(binding.root)

    private fun getDominantColor(holder: WatchListAdapter.WatchListViewHolder, view: View, position: Int){
        if(holder.binding.movieImage2.drawable!=null){
            val drawable: BitmapDrawable = holder.binding.movieImage2.drawable as BitmapDrawable
            val bitmap: Bitmap =drawable.bitmap
            Palette.Builder(bitmap).generate {
                val vibrantColor=it!!.getVibrantColor(ContextCompat.getColor(holder.itemView.context,R.color.white))
                val action= WatchListFragmentDirections.actionWatchListFragmentToDetailsFragment(watch[position].field_id,vibrantColor,watch[position].isTvShow)
                Navigation.findNavController(view).navigate(action)
            }
        }
    }
}