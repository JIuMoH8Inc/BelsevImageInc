package com.example.picturegallery.feature.albums.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.picturegallery.R
import com.example.picturegallery.databinding.AlbumItemBinding
import com.example.picturegallery.feature.albums.uistate.AlbumAdapterUiState

class AlbumAdapter(
    private val onItemClick: (Int) -> Unit
) :
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
        (holder as AlbumViewHolder).bind(albumList[position], onItemClick)
    }

    class AlbumViewHolder(private val binding: AlbumItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            albumUiState: AlbumAdapterUiState,
            onAlbumCLick: (Int) -> Unit
        ) = with(binding) {
            albumThumbnail.setImageBitmap(albumUiState.albumThumb)

            more.setOnClickListener {
                popupMenu(it)
            }

            root.setOnClickListener {
                onAlbumCLick(albumUiState.id)
            }
        }

        private fun popupMenu(view: View) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.album_popup_menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.edit -> {
                        true
                    }
                    R.id.remove -> {
                        true
                    }
                    else -> false
                }
            }
            try {
                val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                popup.isAccessible = true
                val menu = popup.get(popupMenu)
                menu.javaClass
                    .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(menu, true)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                popupMenu.show()
            }
            view.setOnLongClickListener {
                try {
                    val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                    popup.isAccessible = true
                    val menu = popup.get(popupMenu)
                    menu.javaClass
                        .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                        .invoke(menu, true)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    popupMenu.show()
                }
                true
            }
        }
    }
}
