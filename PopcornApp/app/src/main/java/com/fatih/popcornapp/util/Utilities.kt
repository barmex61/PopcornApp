package com.fatih.popcornapp.util


import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.fatih.popcornapp.R
import com.fatih.popcornapp.adapter.MovieAdapter
import com.fatih.popcornapp.view.MainFragmentDirections

import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
const val BASE_URL="https://api.themoviedb.org/3/"
const val API_KEY="ae624ef782f69d5092464dffa234178b"
@BindingAdapter("android:imageUrl")
fun getImage(view: ImageView, url:String?){
    view.alpha=0.2f
    try {
        url?.let {

            Picasso.get().load("https://www.themoviedb.org/t/p/w600_and_h900_bestv2$url").noFade().placeholder(
                R.drawable.popcorn2)
                .into(view,object : Callback {
                    override fun onSuccess() {
                        view.animate().alpha(1f).setDuration(600).start()
                    }

                    override fun onError(e: java.lang.Exception?) {

                    }

                })

        }
    }catch (e:Exception){


    }
}
