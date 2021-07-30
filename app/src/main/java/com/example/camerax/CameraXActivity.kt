package com.example.camerax

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.camerax.data.model.Photo
import com.example.camerax.view.ui.AlbumPhotoFullView
import com.example.camerax.view.ui.AlbumPhotos
import com.example.camerax.view.ui.CameraXFragment
import com.example.camerax.view.ui.IPViewClick
import com.example.camerax.viewmodel.PhotoViewModel
import kotlinx.android.synthetic.main.activity_main.*

class CameraXActivity : AppCompatActivity(), IPhotoViewClick, IPViewClick, AlbumListener {

    lateinit var viewModel: PhotoViewModel
    private lateinit var albumPhotoList: List<Photo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = AlbumViewAdapter(this,this)
        recyclerView.adapter = adapter

        recyclerView.layoutManager = GridLayoutManager(this,3)
        viewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(PhotoViewModel::class.java)

        viewModel.allAlbum.observe(this, Observer {
                list ->
            list?.let{
                adapter.updateAlbumList(it)
            }
        })

        if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(
                        this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

    }

    fun openCamera(view: View) {

        if (!allPermissionsGranted()) {
          Toast.makeText(this, "Please Grant Permission to use CameraX", Toast.LENGTH_SHORT).show()
            return
        }

        supportFragmentManager.beginTransaction()
                     .replace(R.id.frameLayout, CameraXFragment(viewModel,this))
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

    override fun onItemClicked(albumName: String) {
        albumPhotoList = viewModel.allPhotosInAlbum(albumName)

        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, AlbumPhotos(albumPhotoList,this))
            .addToBackStack(null)
            .commit()
    }

    override fun onPClicked(albumName: String) {
        Log.d(TAG," AlbumName: $albumName  ")
        this.onItemClicked(albumName)
    }

    override fun onAlbumItemClicked(position: Int) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, AlbumPhotoFullView(albumPhotoList,position))
                .addToBackStack(null)
                .commit()
    }

}