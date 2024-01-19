package com.example.picturegallery.feature.photos.adapter.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.picturegallery.feature.photos.uistate.PhotosAdapterUiState

class PhotoDiffUtil(
    private val prevPhotoList: List<PhotosAdapterUiState>,
    private val actualPhotoList: List<PhotosAdapterUiState>
) : DiffUtil.Callback() {
    override fun getOldListSize() = prevPhotoList.size

    override fun getNewListSize() = actualPhotoList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        prevPhotoList[oldItemPosition].let { oldItem ->
            actualPhotoList[newItemPosition].let { newItem ->
                oldItem.hashCode() == newItem.hashCode()
            }
        }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        prevPhotoList[oldItemPosition].let { oldItem ->
            actualPhotoList[newItemPosition].let { newItem ->
                oldItem == newItem
            }
        }
}