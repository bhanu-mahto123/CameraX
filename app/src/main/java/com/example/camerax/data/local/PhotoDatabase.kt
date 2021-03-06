package com.example.camerax.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.camerax.data.local.dao.PhotoDao
import com.example.camerax.data.model.Photo

@Database(entities = [Photo::class], version = 1, exportSchema = false)
abstract class PhotoDatabase: RoomDatabase() {

    abstract fun getPhotoDao(): PhotoDao

    companion object{
        @Volatile
        private var INSTANCE: PhotoDatabase? = null
        fun getDatabase(context: Context): PhotoDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PhotoDatabase::class.java,
                    "photo_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}