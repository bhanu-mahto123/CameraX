package com.example.camerax.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.camerax.data.model.Album
import com.example.camerax.data.model.Photo

@Dao
interface PhotoDao {
    @Insert
    suspend fun insert(photo: Photo)

    @Query("Select * from photo_table order by fileId ASC")
    fun getAllPhotos(): LiveData<List<Photo>>

    @Query("Select Count(fileId), filepath, album from photo_table group by album order by fileId ASC")
    fun getAllAlbums(): LiveData<List<Album>>

    @Query("Select * from photo_table where album Like :albumName")
    suspend fun getAllPhotosInAlbum(albumName: String) : List<Photo>

}