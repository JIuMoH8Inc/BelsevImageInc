package com.example.picturegallery.feature.photos.photo_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.picturegallery.databinding.PhotoItemBinding

class PhotosAdapter(private val onPhotoClick: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var photoList: MutableList<PhotosAdapterUiState> = mutableListOf()

    fun submitList(photos: List<PhotosAdapterUiState>) = with(photoList) {
        DiffUtil.calculateDiff(PhotoDiffUtil(this, photos)).run {
            addAll(photos)
            dispatchUpdatesTo(this@PhotosAdapter)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PhotosViewHolder(
            PhotoItemBinding.inflate(inflater, parent, false)
        )
    }

    override fun getItemCount() = photoList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PhotosViewHolder).bind(photoList[position], onPhotoClick)
    }
}