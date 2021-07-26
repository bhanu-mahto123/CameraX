package com.example.camerax

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class PhotoViewModel(application: Application): AndroidViewModel(application) {
    val allPhotos: LiveData<List<Photo>>
    val allAlbum: LiveData<List<Album>>
    private val repository: PhotoRepository
    init{
        val dao = PhotoDatabase.getDatabase(application).getPhotoDao()
        repository = PhotoRepository(dao)
        allPhotos = repository.allPhotos
        allAlbum = repository.allAlbum
    }

    fun insertPhotos(photo: Photo) = viewModelScope.launch (Dispatchers.IO){
        repository.insert(photo)
    }

    fun allPhotosinAlbum(albumName: String): List<Photo>  {
        val list: List<Photo>
        runBlocking {
            val listDeferred = async { repository.allPhotosinAlbum(albumName) }
            list = listDeferred.await()
        }
        return list
    }
}