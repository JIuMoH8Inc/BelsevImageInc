package com.example.picturegallery.injection.module

import com.example.picturegallery.data.repository.AlbumRepositoryImpl
import com.example.picturegallery.data.repository.PhotosRepositoryImpl
import com.example.picturegallery.domain.repository.AlbumRepository
import com.example.picturegallery.domain.repository.PhotosRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAlbumRepository(albumRepository: AlbumRepositoryImpl): AlbumRepository

    @Binds
    @Singleton
    abstract fun bindPhotosRepository(photosRepository: PhotosRepositoryImpl): PhotosRepository
}