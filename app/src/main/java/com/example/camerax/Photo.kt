package com.example.camerax

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_table")
class Photo(val flePath: String,val albumName: String,val dateTaken: String) {
    @PrimaryKey(autoGenerate = true) var fileId = 0
}