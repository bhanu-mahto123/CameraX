package com.example.camerax.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.camerax.R
import com.example.camerax.data.model.Photo
import com.squareup.picasso.Picasso

class AlbumPhotoFullViewAdapter(val context: Context, val albumPhotoList: List<Photo>, val position: Int): RecyclerView.Adapter<AlbumPhotoFullViewAdapter.AlbumFullViewHolder>() {
    inner class AlbumFullViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView = itemView.findViewById<ImageView>(R.id.itemFullImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumFullViewHolder {
        return AlbumFullViewHolder(LayoutInflater.from(context).inflate(R.layout.item_full_photo,parent,false))
    }

    override fun onBindViewHolder(holder: AlbumFullViewHolder, position: Int) {
        Picasso.with(context).load("file://" + albumPhotoList.get(position).flePath).resize(1000,1000).into(holder.imageView)
    }

    override fun getItemCount(): Int {
       return albumPhotoList.size
    }
}