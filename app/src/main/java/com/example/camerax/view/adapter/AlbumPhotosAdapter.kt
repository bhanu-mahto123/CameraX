package com.example.camerax

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.camerax.data.model.Photo
import com.squareup.picasso.Picasso

class AlbumPhotosAdapter(val context: Context, val allPhotos: List<Photo>, val aListener: AlbumListener): RecyclerView.Adapter<AlbumPhotosAdapter.AlbumPhotoViewHolder>() {

    inner class AlbumPhotoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView = itemView.findViewById<ImageView>(R.id.itemImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumPhotoViewHolder {
        val viewHolder = AlbumPhotoViewHolder(LayoutInflater.from(context).inflate(R.layout.item_photo,parent,false))
        viewHolder.imageView.setOnClickListener {
            aListener.onAlbumItemClicked(viewHolder.adapterPosition)
        }
        return viewHolder
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


interface AlbumListener{
    fun onAlbumItemClicked(position: Int)
}