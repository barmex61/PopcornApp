package com.fatih.popcornapp.util


import android.widget.ImageView
import androidx.databinding.BindingAdapter

import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

@BindingAdapter("android:imageUrl")
fun getImage(view: ImageView, url:String?){
    view.alpha=0.2f
    try {
        url?.let {

            Picasso.get().load("https://www.themoviedb.org/t/p/w600_and_h900_bestv2$url").noFade()
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
