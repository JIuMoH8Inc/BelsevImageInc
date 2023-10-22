package com.example.picturegallery.feature.photos.adapter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.picturegallery.databinding.PhotoItemBinding
import com.example.picturegallery.feature.photos.adapter.viewholder.PhotosViewHolder
import com.example.picturegallery.feature.photos.PhotosAdapterUiState
//TODO Вынести в общий блок, так как используется в нескольких фрагментах
class PhotosAdapter(
    private val onPhotoclick: (Int) -> Unit,
    private val onPhotoSelect: (Int, Boolean) -> Unit
) : ListAdapter<PhotosAdapterUiState, PhotosViewHolder>(DiffCallback) {

    companion object {
        var isSelectionMode = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PhotosViewHolder(
            PhotoItemBinding.inflate(inflater, parent, false),
            onPhotoclick,
            onPhotoSelect
        )
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: PhotosViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        when (payloads.lastOrNull()) {
            true -> {
                holder.bindSelected(getItem(position))
            }
            else -> {
                onBindViewHolder(holder, position)
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<PhotosAdapterUiState>() {
        override fun areItemsTheSame(
            oldItem: PhotosAdapterUiState,
            newItem: PhotosAdapterUiState
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: PhotosAdapterUiState,
            newItem: PhotosAdapterUiState
        ) = oldItem == newItem

        override fun getChangePayload(
            oldItem: PhotosAdapterUiState,
            newItem: PhotosAdapterUiState
        ): Any? = if (oldItem.isSelected != newItem.isSelected) true else null

    }
}