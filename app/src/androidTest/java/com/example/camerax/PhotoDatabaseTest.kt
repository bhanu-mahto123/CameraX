package com.example.camerax


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.camerax.data.model.Photo
import com.example.camerax.data.local.dao.PhotoDao
import com.example.camerax.data.local.PhotoDatabase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class PhotoDatabaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var photoDao: PhotoDao
    private lateinit var photoDatabase: PhotoDatabase


    @Before
    fun setUp() {
        photoDatabase = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                PhotoDatabase::class.java
        ).allowMainThreadQueries().build()
        photoDao = photoDatabase.getPhotoDao()
    }

    @After
    fun closeDb() {
        photoDatabase.close()
    }

    @Test
    fun dbInsertionTest()  = runBlockingTest{
        val photo = Photo("abc.jpg","Album  1", "23082021")
        val photo1 = Photo("abcd.jpg","Album  2", "23082021")

        photoDao.insert(photo)
        photoDao.insert(photo1)

        val photoList = photoDao.getAllPhotos().getOrAwaitValue()

        assertThat(photoList).contains(photo)
        assertThat(photoList).contains(photo1)
    }

    @Test
    fun dbAlbumNameTest()  = runBlockingTest{
        val photo = Photo("abc.jpg","Album  1", "23082021")
        photoDao.insert(photo)
        val photoList = photoDao.getAllPhotosInAlbum("Album  1")
        assertThat(photoList).contains(photo)
    }

    @Test
    fun dbGetAlbumTest()  = runBlockingTest{
        val photo = Photo("abc.jpg","Album  1", "23082021")
        val photo1 = Photo("abcd.jpg","Album  2", "23082021")
        val photo2 = Photo("abcde.jpg","Album  2", "23082021")

        photoDao.insert(photo)
        photoDao.insert(photo1)
        photoDao.insert(photo2)

        val photoList = photoDao.getAllAlbums().getOrAwaitValue()

        assertThat(photoList.get(0).album).isEqualTo("Album  1")
        assertThat(photoList.get(0).filepath).isEqualTo("abc.jpg")
        assertThat(photoList.get(0).count).isEqualTo(1)

        assertThat(photoList.get(1).album).isEqualTo("Album  2")
        assertThat(photoList.get(1).filepath).isEqualTo("abcde.jpg")
        assertThat(photoList.get(1).count).isEqualTo(2)
    }


}