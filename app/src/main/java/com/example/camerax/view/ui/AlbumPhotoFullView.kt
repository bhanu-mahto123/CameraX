package com.example.camerax.view.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.camerax.R
import com.example.camerax.data.model.Photo
import com.example.camerax.view.adapter.AlbumPhotoFullViewAdapter


class AlbumPhotoFullView(val albumPhotoList: List<Photo>, val position: Int) : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_album_full_view, container, false)
        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)
        val context: Context = activity?.baseContext!!
        val adapter = AlbumPhotoFullViewAdapter(context,albumPhotoList,position)
        viewPager.adapter = adapter

        viewPager.post{
            viewPager.setCurrentItem(position,true)
        }
        return view
    }

}