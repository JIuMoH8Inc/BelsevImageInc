package com.example.picturegallery.domain.useCase

import android.util.Log
import com.example.picturegallery.domain.repository.AlbumRepository
import com.example.picturegallery.feature.albums.uistate.AlbumAdapterUiState
import javax.inject.Inject

class GetAlbumUseCase @Inject constructor(
    private val albumRepository: AlbumRepository
) {
    suspend operator fun invoke(): List<AlbumAdapterUiState> {
        val albumAdapterUiStateList: MutableList<AlbumAdapterUiState> = mutableListOf()
        val albumList = albumRepository.getAlbumList()
        albumList.forEach { (id, name, albumDescription, coverPhoto, itemCount) ->
            albumAdapterUiStateList.add(
                AlbumAdapterUiState(
                    id = id,
                    itemCount = itemCount,
                    albumName = name,
                    albumThumb = AlbumAdapterUiState.getAlbumThumb(coverPhoto.thumbnail),
                    albumDescription = albumDescription
                )
            )
        }
        return albumAdapterUiStateList
    }
}