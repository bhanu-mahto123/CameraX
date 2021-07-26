package com.example.camerax

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class AlbumPhotoAdapter(val context: Context, val allPhotos: List<Photo>): RecyclerView.Adapter<AlbumPhotoAdapter.AlbumPhotoViewHolder>() {

    inner class AlbumPhotoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView = itemView.findViewById<ImageView>(R.id.itemImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumPhotoViewHolder {
        return AlbumPhotoViewHolder(LayoutInflater.from(context).inflate(R.layout.item_photo,parent,false))
    }

    override fun onBindViewHolder(holder: AlbumPhotoViewHolder, position: Int) {
       val currentPhoto = allPhotos[position]
//        holder.imageView.setImageBitmap(BitmapFactory.decodeFile(currentPhoto.flePath))

        Picasso.with(context).load("file://" + currentPhoto.flePath).fit().into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return allPhotos.size
    }

}