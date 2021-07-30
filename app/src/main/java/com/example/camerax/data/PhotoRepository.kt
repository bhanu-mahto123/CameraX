package com.example.camerax.data

import androidx.lifecycle.LiveData
import com.example.camerax.data.model.Album
import com.example.camerax.data.local.dao.PhotoDao
import com.example.camerax.data.model.Photo

class PhotoRepository(private val photoDao: PhotoDao) {

    val allAlbum: LiveData<List<Album>> = photoDao.getAllAlbums()

    suspend fun insert(photo: Photo){
        photoDao.insert(photo)
    }

     suspend fun allPhotosInAlbum(albumName: String): List<Photo> {
        return photoDao.getAllPhotosInAlbum(albumName)
    }

}