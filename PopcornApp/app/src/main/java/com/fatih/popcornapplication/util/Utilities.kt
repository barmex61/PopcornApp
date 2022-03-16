package com.fatih.popcornapplication.util


import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.fatih.popcornapplication.R
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
