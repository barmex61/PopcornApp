package com.fatih.popcornapplication.dependecyInjection

import android.content.Context
import androidx.room.Room
import com.fatih.popcornapplication.repositories.ModelRepositories
import com.fatih.popcornapplication.repositories.ModelRepositoriesInterface
import com.fatih.popcornapplication.room.RoomDao
import com.fatih.popcornapplication.room.RoomDb
import com.fatih.popcornapplication.service.MovieApi
import com.fatih.popcornapplication.util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object Module {

    @Provides
    @Singleton
    fun injectRoomDatabase(@ApplicationContext context: Context)= Room.databaseBuilder(context,
        RoomDb::class.java,"Room Database").build()
    @Provides
    @Singleton
    fun injectRoomDao(roomDb: RoomDb)=roomDb.roomDao()
    @Provides
    @Singleton
    fun injectRetrofit()=Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(MovieApi::class.java)
    @Provides
    @Singleton
    fun injectRepositories(roomDao: RoomDao, movieApi: MovieApi)=ModelRepositories(roomDao,movieApi) as ModelRepositoriesInterface

}