package com.example.picturegallery.domain.useCase

import com.example.picturegallery.R
import com.example.picturegallery.domain.manager.ResourceManager
import com.example.picturegallery.domain.repository.AlbumRepository
import com.example.picturegallery.feature.albums.AlbumAdapterUiState
import com.example.picturegallery.feature.albums.AlbumUnited
import javax.inject.Inject

class GetAlbumUseCase @Inject constructor(
    private val albumRepository: AlbumRepository,
    private val resourceManager: ResourceManager
) {
    suspend operator fun invoke(): AlbumUnited {
        val albumAdapterUiStateList: MutableList<AlbumAdapterUiState> = mutableListOf()
        val albumList = albumRepository.getAlbumList()
        albumList.forEach { (id, name, desc, coverPhoto, itemCount) ->
            albumAdapterUiStateList.add(
                AlbumAdapterUiState(
                    id = id,
                    itemCount = resourceManager.getString(R.string.album_count_item_pattern, itemCount.toString()),
                    albumName = name,
                    albumThumb = AlbumAdapterUiState.getAlbumThumb(coverPhoto.thumbnail),
                    albumDesc = desc
                )
            )
        }
        return AlbumUnited(
            albumList,
            albumAdapterUiStateList
        )
    }
}