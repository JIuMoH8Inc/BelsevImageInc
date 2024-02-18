package com.example.picturegallery.feature.albums.adapter.viewholder

import android.view.View
import androidx.appcompat.widget.PopupMenu

import androidx.recyclerview.widget.RecyclerView
import com.example.picturegallery.R
import com.example.picturegallery.databinding.AlbumCardBinding
import com.example.picturegallery.feature.albums.AlbumAdapterUiState

class AlbumViewHolder(
    private val binding: AlbumCardBinding,
    private val onItemClick: (Int) -> Unit,
    private val onMenuItemClick: (Int, Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(albumUiState: AlbumAdapterUiState) = with(binding) {
        albumThumb.setImageBitmap(albumUiState.albumThumb)
        albumTitle.text = albumUiState.albumName
        albumItemCount.isSelected = true
        albumItemCount.text = albumUiState.itemCount
        with(albumDescription) {
            text = albumUiState.albumDesc
        }

        root.setOnClickListener {
            onItemClick(albumUiState.id)
        }

        more.setOnClickListener {
            createPopUpMenu(it, albumUiState.id)
        }
    }

    private fun createPopUpMenu(view: View, albumId: Int) {
        PopupMenu(view.context, view).apply {
            menuInflater.inflate(R.menu.album_menu, menu)
            setForceShowIcon(true)
            setOnMenuItemClickListener { menuItem ->
                onMenuItemClick(menuItem.itemId, albumId)
                true
            }
            show()
        }
    }
}