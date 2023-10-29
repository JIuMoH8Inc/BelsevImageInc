package com.example.picturegallery.feature.photos.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.picturegallery.databinding.PhotoItemBinding
import com.example.picturegallery.feature.photos.uistate.PhotosAdapterUiState

class PhotosViewHolder(private val binding: PhotoItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(photo: PhotosAdapterUiState, onPhotoClick: (Int) -> Unit) = with(binding) {
        photoContainer.setImageBitmap(photo.picture)
        titleItem.text = photo.title
        dateCreationItem.text = photo.creationDate

        root.setOnClickListener {
            onPhotoClick(photo.id)
        }

    }
}