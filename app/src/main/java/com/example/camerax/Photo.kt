package com.example.camerax

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_table")
data class Photo(@ColumnInfo(name="filepath")
            val flePath: String,
            @ColumnInfo(name="album")
            val albumName: String,
            @ColumnInfo(name="date")
            val dateTaken: String) {
    @PrimaryKey(autoGenerate = true) var fileId = 0
}