package com.example.camerax.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.camerax.data.model.Album
import com.example.camerax.data.model.Photo
import com.example.camerax.data.local.PhotoDatabase
import com.example.camerax.data.PhotoRepository
import kotlinx.coroutines.*

class PhotoViewModel(application: Application): AndroidViewModel(application) {

    val allAlbum: LiveData<List<Album>>
    private val repository: PhotoRepository

    init{
        val dao = PhotoDatabase.getDatabase(application).getPhotoDao()
        repository = PhotoRepository(dao)
        allAlbum = repository.allAlbum
    }

    fun insertPhotos(photo: Photo) = viewModelScope.launch (Dispatchers.IO){
        repository.insert(photo)
    }

    fun allPhotosInAlbum(albumName: String): List<Photo>  {
        val list: List<Photo>
        runBlocking {
            val listDeferred = async { repository.allPhotosInAlbum(albumName) }
            list = listDeferred.await()
        }
        return list
    }

}