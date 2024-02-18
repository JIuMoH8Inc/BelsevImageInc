package com.example.picturegallery.feature.photos.adapter

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.picturegallery.databinding.PhotoItemBinding
import com.example.picturegallery.feature.photos.PhotosAdapterUiState

class PhotosViewHolder(
    private val binding: PhotoItemBinding,
    private val onPhotoClick: (Int) -> Unit,
    private val onPhotoSelect: (Int, Boolean) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        var isSelectionMode = false
    }

    init {
        with(binding.root) {
            setOnClickListener {
                if (isSelectionMode) {
                    onPhotoSelect(selectedPhoto.id, selectedPhoto.isSelected)
                } else onPhotoClick(selectedPhoto.id)
            }

            setOnLongClickListener {
                if (!isSelectionMode) {
                    onPhotoSelect(selectedPhoto.id, selectedPhoto.isSelected)
                }
                true
            }
        }
    }

    private lateinit var selectedPhoto: PhotosAdapterUiState
    fun bind(photo: PhotosAdapterUiState) {
        selectedPhoto = photo
        with(binding) {
            Glide.with(root.context)
                .asBitmap()
                .centerCrop()
                .load(photo.picture)
                .into(photoContainer)
        }

    }

    fun bindSelected(photo: PhotosAdapterUiState) {
        selectedPhoto = photo
        with(binding) {
            selectedFrame.isVisible = photo.isSelected
        }
    }
}