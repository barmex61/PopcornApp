package com.fatih.popcornapplication.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fatih.popcornapplication.model.RoomEntity

@Database(entities = [RoomEntity::class], version = 1)
abstract class RoomDb: RoomDatabase() {
    abstract fun roomDao(): RoomDao
}
