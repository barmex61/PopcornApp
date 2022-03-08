package com.fatih.popcornapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fatih.popcornapp.R
import com.fatih.popcornapp.databinding.EpisodesLayoutRowBinding
import com.fatih.popcornapp.model.TvShowSeason

class SeasonAdapter(private val listTvShowSeason: ArrayList<TvShowSeason>,private val rating:CharSequence,private val genre:CharSequence): RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonViewHolder {
        val binding=DataBindingUtil.inflate<EpisodesLayoutRowBinding>(LayoutInflater.from(parent.context),
            R.layout.episodes_layout_row,parent,false)
        return SeasonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeasonViewHolder, position: Int) {
        holder.binding.tvShowSeason=listTvShowSeason[position]
        holder.binding.ratingText2.text="$rating /10"
        holder.binding.genre= genre.toString()
        println(holder.binding.ratingText2.text)
    }

    override fun getItemCount(): Int {
            return listTvShowSeason.size
    }
    class SeasonViewHolder(val binding:EpisodesLayoutRowBinding) : RecyclerView.ViewHolder(binding.root)

}