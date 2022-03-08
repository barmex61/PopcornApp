package com.fatih.popcornapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fatih.popcornapp.model.RoomEntity

@Database(entities = [RoomEntity::class], version = 1)
abstract class RoomDb: RoomDatabase() {
    abstract fun roomDao():RoomDao
    companion object{
        @Volatile private var roomDb:RoomDb?=null
        operator fun invoke(context:Context)= roomDb?: synchronized(Any()){
            roomDb?:createRoomDatabase(context).also {
                roomDb=it
            }
        }
    private fun createRoomDatabase(context: Context):RoomDb{
        return Room.databaseBuilder(context,RoomDb::class.java,"Room Database").build()
    }
    }
}