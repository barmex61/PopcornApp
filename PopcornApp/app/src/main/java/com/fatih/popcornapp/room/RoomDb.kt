package com.fatih.popcornapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fatih.popcornapp.model.RoomEntity

@Database(entities = [RoomEntity::class], version = 1)
abstract class RoomDb: RoomDatabase() {
    abstract fun roomDao():RoomDao
}
