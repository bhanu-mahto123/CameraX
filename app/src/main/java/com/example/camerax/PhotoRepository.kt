package com.example.camerax

import androidx.lifecycle.LiveData

class PhotoRepository(private val photoDao: PhotoDao) {
    val allPhotos: LiveData<List<Photo>> = photoDao.getAllPhotos()
    val allAlbum: LiveData<List<Album>> = photoDao.getAllALbums()



    suspend fun insert(photo: Photo){
        photoDao.insert(photo)
    }

     suspend fun allPhotosinAlbum(albumName: String): List<Photo> {
        return photoDao.getAllPhotosinAlbum(albumName)
    }

}