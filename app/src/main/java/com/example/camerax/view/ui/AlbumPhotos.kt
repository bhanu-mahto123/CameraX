package com.example.camerax.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.camerax.AlbumListener
import com.example.camerax.AlbumPhotosAdapter
import com.example.camerax.R
import com.example.camerax.data.model.Photo
import kotlinx.android.synthetic.main.activity_main.*


/**
 * A simple [Fragment] subclass.
 * Use the [AlbumPhotos.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumPhotos(val albumList: List<Photo>, val albumListener: AlbumListener) : Fragment() {

    private lateinit var mRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val context = activity?.baseContext!!
        val view: View =inflater.inflate(R.layout.fragment_album_photo, container, false)
        mRecyclerView = view.findViewById(R.id.recyclerAlbumPhotoView)
        val adapter = AlbumPhotosAdapter(context,albumList,albumListener)
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = GridLayoutManager(context,3)
        return view
    }

}