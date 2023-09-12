package com.example.picturegallery.feature.photos.adapter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.picturegallery.databinding.PhotoItemBinding
import com.example.picturegallery.feature.photos.adapter.viewholder.PhotosViewHolder
import com.example.picturegallery.feature.photos.uistate.PhotosAdapterUiState

class PhotosAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var photoList: MutableList<PhotosAdapterUiState> = mutableListOf()

    fun submitList(photos: List<PhotosAdapterUiState>) {
        val index = photoList.lastIndex
        photoList.addAll(photos)
        notifyItemRangeInserted(index, photoList.lastIndex)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PhotosViewHolder(
            PhotoItemBinding.inflate(inflater, parent, false)
        )
    }

    override fun getItemCount() = photoList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PhotosViewHolder).bind(photoList[position])
    }
}