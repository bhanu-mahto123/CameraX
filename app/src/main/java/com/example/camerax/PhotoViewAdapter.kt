package com.example.camerax

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class PhotoViewAdapter(val context: Context): RecyclerView.Adapter<PhotoViewAdapter.PhotoViewHolder>() {

    val allPhotos = ArrayList<Photo>()

    inner class PhotoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView = itemView.findViewById<ImageView>(R.id.itemImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val viewHolder = PhotoViewHolder(LayoutInflater.from(context).inflate(R.layout.item_photo,parent,false))
        //add click listner here by interface
        return viewHolder
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentPhoto = allPhotos[position]
        holder.imageView.setImageBitmap(BitmapFactory.decodeFile(currentPhoto.flePath))
    }

    override fun getItemCount(): Int {
        return allPhotos.size
    }

    fun updateList(newList: List<Photo>){
        allPhotos.clear()
        allPhotos.addAll(newList)
        notifyDataSetChanged()
    }
}