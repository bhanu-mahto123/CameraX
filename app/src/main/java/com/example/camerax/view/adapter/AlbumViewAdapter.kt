package com.example.camerax

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.camerax.data.model.Album
import com.squareup.picasso.Picasso

class AlbumViewAdapter(val context: Context, val listner: IPhotoViewClick): RecyclerView.Adapter<AlbumViewAdapter.PhotoViewHolder>() {

    var allAlbums = ArrayList<Album>()

    inner class PhotoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView = itemView.findViewById<ImageView>(R.id.itemImage)
        val textView = itemView.findViewById<TextView>(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val viewHolder = PhotoViewHolder(LayoutInflater.from(context).inflate(R.layout.item_photo,parent,false))

        viewHolder.imageView.setOnClickListener {
            listner.onItemClicked(allAlbums[viewHolder.adapterPosition].album)
        }

        Log.d(TAG,"OnCreateViewHolder() ")
        return viewHolder
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentAlbum = allAlbums[position]

//        holder.imageView.setImageBitmap(BitmapFactory.decodeFile(currentAlbum.filepath))

        Picasso.with(context).load("file://" + currentAlbum.filepath).fit().into(holder.imageView)

        Log.d(TAG,"FilePath: ${currentAlbum.filepath}")

        holder.textView.visibility = View.VISIBLE
        holder.textView.text = currentAlbum.album
    }

    override fun getItemCount(): Int {
        Log.d(TAG,"getItemCount() ${allAlbums.size}")
        return allAlbums.size
    }

    fun updateAlbumList(it: List<Album>) {
        allAlbums.clear()
        allAlbums.addAll(it)
        notifyDataSetChanged()
    }

    companion object{
        val TAG = "PhotoViewAdapter"
    }
}

interface IPhotoViewClick{
    fun onItemClicked(albumName: String)
}