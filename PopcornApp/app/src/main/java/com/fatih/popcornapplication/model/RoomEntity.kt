package com.fatih.popcornapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomEntity(
    @ColumnInfo(name = "lastAirDate")
    val lastAirDate: String,

    @ColumnInfo(name = "posterPath")
    val posterPath: String,

    @ColumnInfo(name = "voteAverage")
    val voteAverage: Double,

    @ColumnInfo(name = "isTvShow")
    val isTvShow:Boolean,

    @PrimaryKey var field_id: Int
    )
