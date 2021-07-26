package com.example.camerax

import androidx.room.ColumnInfo

data class Album (
    @ColumnInfo(name = "Count(fileId)")val count: Int, @ColumnInfo(name = "filepath") val filepath: String,@ColumnInfo(name = "album") val album: String
)