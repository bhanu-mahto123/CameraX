package com.example.camerax

import android.Manifest
import android.content.pm.PackageManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import java.util.concurrent.Executors
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity(), IPhotoViewClick {

    lateinit var viewModel: PhotoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = PhotoViewAdapter(this,this)
        recyclerView.adapter = adapter

        recyclerView.layoutManager = GridLayoutManager(this,3)
        viewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(PhotoViewModel::class.java)

        viewModel.allPhotos.observe(this, Observer {
          list ->
            list?.let{
//                adapter.updatePhotoList(it)
            }
        })

//        adapter.allAlbums = viewModel.allAlbum
        viewModel.allAlbum.observe(this, Observer {
                list ->
            list?.let{
                adapter.updateAlbumList(it)
            }
        }
        )

        if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(
                        this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    fun openCamera(view: View) {
        removeViewFromFrameLayout()
        supportFragmentManager.beginTransaction()
                     .replace(R.id.frameLayout,CameraXFragment(viewModel))
                     .addToBackStack(null)
                     .commit()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
                baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {

            } else {
                Toast.makeText(this,
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        val TAG = "MainActivity"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    fun removeViewFromFrameLayout(){
        val frameLayout = findViewById<FrameLayout>(R.id.frameLayout)
        frameLayout.removeAllViews()
    }
    override fun onItemClicked(album: Album) {
        val list = viewModel.allPhotosinAlbum(album.album)

        removeViewFromFrameLayout()
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout,AlbumPhoto(list))
            .addToBackStack(null)
            .commit()

        for(i in list.indices){

            Log.d(TAG,"all Photo Clicked ${list[i].albumName}  ${list[i].flePath}")

//            Log.d(TAG,"view Clicked ${album.album}  ${album.filepath}")
        }
    }
}