package com.example.camerax

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotoViewModel(application: Application): AndroidViewModel(application) {
    val allPhotos: LiveData<List<Photo>>
    private val repository: PhotoRepository
    init{
        val dao = PhotoDatabase.getDatabase(application).getPhotoDao()
        repository = PhotoRepository(dao)
        allPhotos = repository.allPhotos
    }

    fun insertPhotos(photo: Photo) = viewModelScope.launch (Dispatchers.IO){
        repository.insert(photo)
    }
}