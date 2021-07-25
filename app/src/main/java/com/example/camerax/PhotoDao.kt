package com.example.camerax

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PhotoDao {
    @Insert
    suspend fun insert(photo: Photo)

    @Query("Select * from photo_table order by fileId ASC")
    fun getAllPhotos(): LiveData<List<Photo>>
}