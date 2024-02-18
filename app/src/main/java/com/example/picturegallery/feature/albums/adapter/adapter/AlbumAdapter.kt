package com.example.picturegallery.feature.albums.adapter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.picturegallery.databinding.AlbumCardBinding
import com.example.picturegallery.feature.albums.adapter.viewholder.AlbumViewHolder
import com.example.picturegallery.feature.albums.AlbumAdapterUiState

class AlbumAdapter(
    private val onItemClick: (Int) -> Unit,
    private val onMenuItemClick: (Int, Int) -> Unit
) :
    ListAdapter<AlbumAdapterUiState, AlbumViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AlbumViewHolder(
            AlbumCardBinding.inflate(inflater, parent, false),
            onItemClick,
            onMenuItemClick
        )
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object DiffCallback: DiffUtil.ItemCallback<AlbumAdapterUiState>() {
        override fun areItemsTheSame(
            oldItem: AlbumAdapterUiState,
            newItem: AlbumAdapterUiState
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: AlbumAdapterUiState,
            newItem: AlbumAdapterUiState
        ) = oldItem == newItem

    }
}