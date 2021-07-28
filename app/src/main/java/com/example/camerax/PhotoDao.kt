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

    @Query("Select Count(fileId), filepath, album from photo_table group by album order by fileId ASC")
    fun getAllALbums(): LiveData<List<Album>>

    @Query("Select * from photo_table where album Like :albumName")
    suspend fun getAllPhotosinAlbum(albumName: String) : List<Photo>
}