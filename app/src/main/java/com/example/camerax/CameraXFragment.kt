package com.example.camerax

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.util.concurrent.Executors

/**
 * A simple [Fragment] subclass.
 * Use the [CameraXFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CameraXFragment(val viewModel: PhotoViewModel) : Fragment() {

    private lateinit var ivCapture: ImageView
    private lateinit var ivViewImage: ImageView
    private lateinit var viewFinder: PreviewView
    private val executor = Executors.newSingleThreadExecutor()
    private val imageCapture = ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build()
    private lateinit var outputDirectory: File
    private var camera: Camera? = null

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
            activity?.onBackPressed()
        }

        outputDirectory = getOutputDirectory(activity?.baseContext!!)

        viewFinder.post { startCamera() }

        return view
    }

    private fun startCamera() {


        configureCamera(activity?.baseContext!!)

        ivCapture.setOnClickListener {
            clickAndSavePhoto()
        }

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

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(context))

    }


    private fun clickAndSavePhoto(){
        val photoFile = File(outputDirectory,"${System.currentTimeMillis()}.jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {

            override fun onError(exc: ImageCaptureException) {
                Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                val msg = "Photo capture succeeded: $savedUri"
                activity?.runOnUiThread(Runnable {
                    val toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
                    toast.show()
                    ivViewImage.setImageBitmap(BitmapFactory.decodeFile(photoFile.absolutePath))
                })
                viewModel.insertPhotos(Photo(photoFile.absolutePath,"Bitch","${System.currentTimeMillis()}"))
                Log.d(TAG, msg)
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