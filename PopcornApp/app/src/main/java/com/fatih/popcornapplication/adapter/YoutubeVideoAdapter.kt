package com.fatih.popcornapplication.adapter

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fatih.popcornapplication.databinding.FragmentTrailerRowBinding
import com.fatih.popcornapplication.model.İtem
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.pow

class YoutubeVideoAdapter @Inject constructor(): RecyclerView.Adapter<YoutubeVideoAdapter.YoutubeViewHolder>() {
    var youtubeList=ArrayList<İtem>()
    @SuppressLint("SimpleDateFormat")
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    var listener:((Int)->Unit)?=null
    fun setOnItemClickListener(listener:(Int)->Unit){
        this.listener=listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeViewHolder {
        val binding=FragmentTrailerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return YoutubeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YoutubeViewHolder, position: Int) {

        try {
            holder.itemView.setOnClickListener {
                    listener?.let {
                        it(position)
                    }
            }
            val dateStr = youtubeList[position].snippet.publishedAt
            val date= inputFormat.parse(dateStr)
            val niceDateStr = DateUtils.getRelativeTimeSpanString(date.time, Calendar.getInstance().timeInMillis, DateUtils.DAY_IN_MILLIS)
            holder.binding.channelTittle=youtubeList[position].snippet.channelTitle
            holder.binding.description=youtubeList[position].snippet.description
            holder.binding.imageUrl=youtubeList[position].snippet.thumbnails.high.url
            holder.binding.viewText=coolNumberFormat(youtubeList[position].statistics.viewCount.toLong())
            holder.binding.titleText= youtubeList[position].snippet.title
            holder.binding.dateText=niceDateStr.toString()
        }catch (e:Exception){
            println(e.message)
        }

    }

    override fun getItemCount(): Int {
        return youtubeList.size
    }
    class YoutubeViewHolder(val binding:FragmentTrailerRowBinding) :RecyclerView.ViewHolder(binding.root)

    fun coolNumberFormat(count: Long): String {
        if (count < 1000) return "" + count
        val exp = (Math.log(count.toDouble()) / Math.log(1000.0)).toInt()
        val format = DecimalFormat("0.#")
        val value: String = format.format(count / 1000.0.pow(exp.toDouble()))
        return String.format("%s%c", value, "kMBTPE"[exp - 1])
    }

}