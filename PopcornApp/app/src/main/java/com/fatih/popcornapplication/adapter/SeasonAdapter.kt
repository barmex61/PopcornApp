package com.fatih.popcornapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fatih.popcornapplication.R
import com.fatih.popcornapplication.databinding.EpisodesLayoutRowBinding
import com.fatih.popcornapplication.model.TvShowSeason
import javax.inject.Inject

class SeasonAdapter @Inject constructor( ): RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder>() {
    var rating:CharSequence=""
    var genre:CharSequence=""
    private val diffUtil=object:DiffUtil.ItemCallback<TvShowSeason>(){
        override fun areContentsTheSame(oldItem: TvShowSeason, newItem: TvShowSeason): Boolean {
            return oldItem==newItem
        }

        override fun areItemsTheSame(oldItem: TvShowSeason, newItem: TvShowSeason): Boolean {
            return oldItem==newItem
        }
    }
    private val listDiffer=AsyncListDiffer(this,diffUtil)
    var listTvShowSeason: List<TvShowSeason>
        get() = listDiffer.currentList
        set(value) = listDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonViewHolder {
        val binding=DataBindingUtil.inflate<EpisodesLayoutRowBinding>(LayoutInflater.from(parent.context),
            R.layout.episodes_layout_row,parent,false)
        return SeasonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeasonViewHolder, position: Int) {
        holder.binding.tvShowSeason=listTvShowSeason[position]
        holder.binding.ratingText2.text="$rating /10"
        holder.binding.genre= genre.toString()
        var drawable=R.drawable.cardview_background
        when(position%5){
            0->drawable=R.drawable.cardview_background
            1->drawable=R.drawable.cardview_background2
            2->drawable=R.drawable.cardview_background3
            3->drawable=R.drawable.cardview_background4
            4->drawable=R.drawable.cardview_background1
        }
        holder.binding.backgroundLayout.background=ContextCompat.getDrawable(holder.binding.root.context,drawable)
    }

    override fun getItemCount(): Int {
            return listTvShowSeason.size
    }
    class SeasonViewHolder(val binding:EpisodesLayoutRowBinding) : RecyclerView.ViewHolder(binding.root)

}