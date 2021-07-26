package com.example.camerax

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


/**
 * A simple [Fragment] subclass.
 * Use the [AlbumPhoto.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumPhoto(val albumList: List<Photo>) : Fragment() {

    private lateinit var mRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val context = activity?.baseContext!!
        val view: View =inflater.inflate(R.layout.fragment_album_photo, container, false)
        mRecyclerView = view.findViewById(R.id.recyclerAlbumPhotoView)
        val adapter = AlbumPhotoAdapter(context,albumList)
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = GridLayoutManager(context,3)
        return view
    }

}