package com.example.picturegallery.feature.albums.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.picturegallery.databinding.AlbumItemBinding
import com.example.picturegallery.feature.albums.uistate.AlbumAdapterUiState

class AlbumAdapter(private val onItemClick: (Int) -> Unit, private val onMoreClick: (AlbumAdapterUiState) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var albumList: List<AlbumAdapterUiState> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setNewAlbumList(albumList: List<AlbumAdapterUiState>) {
        this.albumList = albumList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AlbumViewHolder(AlbumItemBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = albumList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AlbumViewHolder).bind(albumList[position], onItemClick, onMoreClick)
    }

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
}