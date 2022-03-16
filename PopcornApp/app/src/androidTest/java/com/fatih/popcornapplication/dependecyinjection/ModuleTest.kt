package com.fatih.popcornapplication.dependecyinjection

import android.content.Context
import androidx.room.Room
import com.fatih.popcornapplication.room.RoomDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
class ModuleTest {

    @Provides
    @Named("testDatabase")
    fun injectRoomDb(@ApplicationContext context:Context)= Room.inMemoryDatabaseBuilder(context,RoomDb::class.java).allowMainThreadQueries().build()

}