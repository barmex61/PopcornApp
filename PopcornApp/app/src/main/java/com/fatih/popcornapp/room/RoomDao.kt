package com.fatih.popcornapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.fatih.popcornapp.model.RoomEntity

@Dao
interface RoomDao {
    @Insert
    suspend fun addTvShow(roomEntity: RoomEntity)
    @Delete
    suspend fun deleteTvShow(roomEntity: RoomEntity)
    @Query("SELECT * FROM RoomEntity")
    suspend fun getAllTvShow():List<RoomEntity>
    @Query("SELECT * FROM RoomEntity WHERE id=:idInput")
    suspend fun getSelectedTvShow(idInput:Int):RoomEntity
}