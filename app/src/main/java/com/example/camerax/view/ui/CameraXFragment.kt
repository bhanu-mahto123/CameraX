package com.example.camerax.view.ui

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.camerax.R
import com.example.camerax.data.model.Photo
import com.example.camerax.viewmodel.PhotoViewModel
import com.squareup.picasso.Picasso
import java.io.File
import java.util.concurrent.Executors



class CameraXFragment(val viewModel: PhotoViewModel, val pListner: IPViewClick) : Fragment() {

    private lateinit var ivCapture: ImageView
    private lateinit var ivViewImage: ImageView
    private lateinit var viewFinder: PreviewView
    private val executor = Executors.newSingleThreadExecutor()
    private val imageCapture = ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build()
    private lateinit var outputDirectory: File
    private var camera: Camera? = null

    var albumNum:Int?=null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_camera_x, container, false)

        ivCapture = view.findViewById(R.id.ivCapture)
        ivViewImage = view.findViewById(R.id.ivLoadImage)
        viewFinder = view.findViewById(R.id.viewFinder)

        ivViewImage.setOnClickListener {
            var c = albumNum
            if(c==null){
                Toast.makeText(activity?.baseContext!!, "You should have some clicked photos. Anyway Let's Go to Albums Home Screen", Toast.LENGTH_SHORT).show()
            }
            activity?.onBackPressed()
            if(c!=null){
                pListner.onPClicked("Album  ${c?.minus(1)}")
            }

        }

        outputDirectory = getOutputDirectory(activity?.baseContext!!)

        viewFinder.post { startCamera() }

        return view
    }



    private fun startCamera() {


        configureCamera(activity?.baseContext!!)

        var mp = MediaPlayer.create(context, R.raw.camera_sound)

        var mHandler: Handler? = null
        var mAction: Runnable = object : Runnable {
            override fun run() {
                clickAndSavePhoto(mp,albumNum!!)
                mHandler!!.postDelayed(this, 500)
            }
        }


       ivCapture.setOnTouchListener(object : OnTouchListener {
           val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)



           @SuppressLint("ClickableViewAccessibility")
           override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                           MotionEvent.ACTION_DOWN -> {
                               albumNum = sharedPref?.getInt(getString(R.string.saved_album_key), resources.getInteger(R.integer.default_album_number))
//                        if (mHandler != null) return true
                         mHandler = Handler()
                        Log.d("CameraX", "On TOuch Down Called")

                        mHandler!!.removeCallbacks(mAction)
                        mHandler!!.postDelayed(mAction, 300)
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        if (mHandler == null) return true
                        Log.d("CameraX", "On TOuch Up Called")
                        mAction.run()
                        mHandler!!.removeCallbacks(mAction)
                        mHandler = null
                        albumNum = albumNum?.plus(1)

                        with(sharedPref?.edit()!!) {
                            putInt(getString(R.string.saved_album_key), albumNum!!)
                            apply()
                        }
                    }
                }
                return true
            }

        })

    }


    private fun configureCamera(context: Context){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

        cameraProviderFuture.addListener(Runnable {

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageCapture)

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(context))

    }


    private fun clickAndSavePhoto(mp: MediaPlayer,albumNum: Int) {

        val photoFile = File(outputDirectory, "${System.currentTimeMillis()}.jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {

            override fun onError(exc: ImageCaptureException) {
                Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
//                val savedUri = Uri.fromFile(photoFile)
//                val msg = "Photo capture succeeded: $savedUri"
                activity?.runOnUiThread(Runnable {
//                    val toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
//                    toast.show()

                    Picasso.with(context).load("file://" + photoFile.absolutePath).fit().into(ivViewImage)
//                    ivViewImage.setImageBitmap(BitmapFactory.decodeFile(photoFile.absolutePath))
                    mp.start()
                })
                viewModel.insertPhotos(Photo(photoFile.absolutePath, "Album  ${albumNum.toString()}", "${System.currentTimeMillis()}"))

//                Log.d(TAG, msg)
            }

        })

    }


    private fun getOutputDirectory(context: Context): File {
        val appContext = context.applicationContext
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else appContext.filesDir
    }


}

interface IPViewClick{
    fun onPClicked(albumName: String)
}