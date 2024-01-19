package com.example.picturegallery.feature.albums.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.picturegallery.databinding.AlbumItemBinding
import com.example.picturegallery.feature.albums.uistate.AlbumAdapterUiState

class AlbumViewHolder(private val binding: AlbumItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(albumUiState: AlbumAdapterUiState, onAlbumCLick: (Int) -> Unit, onMoreClick: (AlbumAdapterUiState) -> Unit)  = with(binding) {
        albumThumbnail.setImageBitmap(albumUiState.albumThumb)

        more.setOnClickListener {
            onMoreClick(albumUiState)
        }

        root.setOnClickListener {
            onAlbumCLick(albumUiState.id)
        }
    }
}