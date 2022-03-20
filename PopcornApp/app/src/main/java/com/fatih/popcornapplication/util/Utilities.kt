package com.fatih.popcornapplication.util


import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.fatih.popcornapplication.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.util.HashMap

const val BASE_URL="https://api.themoviedb.org/3/"
const val API_KEY="ae624ef782f69d5092464dffa234178b"
val tvshow_genre_list= arrayOf("Action","Animation","Comedy","Crime","Documentary","Drama","Family","Kids","Mystery","News","Reality","Science Fiction","Soap","Talk","War","Western")
val movie_genre_list= arrayOf("Action","Adventure","Animation","Comedy","Crime","Documentary","Drama","Family","Fantasy","History","Horror","Music","Mystery","Romance","Science Fiction","TV Movie","Thriller","War","Western")
val movie_booleanArray=BooleanArray(movie_genre_list.size)
val tvshow_booleanArray=BooleanArray(tvshow_genre_list.size)
val tvShowSortArray= arrayOf("Popularity","First Air Date","Vote Average")
val qualityArray= arrayOf("360p","480p","720p","1080p")
val booleanArray2=BooleanArray(qualityArray.size)
val tvShowGenreMap:HashMap<String,Int> = hashMapOf("Action" to 10759,"Animation" to 16,"Comedy" to 35,"Crime" to 80,"Documentary" to 99,"Drame" to 18,"Family" to 10751,"Kids" to 10762,"Mystery" to 9648,"News" to 10763,"Reality" to 10764,"Science Fiction" to 10765,"Soap" to 10766,"Talk" to 10767,"War" to 10768,"Western" to 37)
val movieGenreMap: HashMap<String,Int> = hashMapOf("Action" to 28,"Adventure" to 12,"Animation" to 16,"Comedy" to 35,"Crime" to 80,"Documentary" to 99,"Drama" to 18 ,"Family" to 10751,"Fantasy" to 14,"History" to 36,"Horror" to 27,"Music" to 10402 ,"Mystery" to 9648 ,"Romance" to 10749,"Science Fiction" to 878,"TV Movie" to 10770,"Thriller" to 53 ,"War" to 10752,"Western" to 37 )
val genreList= listOf<Int>(10759,16,35,28,12,80,99,18,10751,10762,9648,14,10763,36,27,10764,10402,10765,10766,10749,10767,10768,10770,37,53,10752)
val movieSortList= listOf<String>("popularity.desc","first_air_date.desc","vote_average.desc")
val tvShowSortList= listOf<String>("popularity.desc","release_date.desc","vote_average.desc")
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
