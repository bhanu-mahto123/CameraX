package com.example.camerax

import androidx.lifecycle.LiveData

class PhotoRepository(private val photoDao: PhotoDao) {
    val allPhotos: LiveData<List<Photo>> = photoDao.getAllPhotos()

    suspend fun insert(photo: Photo){
        photoDao.insert(photo)
    }

}