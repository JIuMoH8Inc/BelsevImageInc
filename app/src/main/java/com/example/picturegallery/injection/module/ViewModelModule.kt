package com.example.picturegallery.injection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.picturegallery.feature.album_content.AlbumContentViewModel
import com.example.picturegallery.feature.albums.AlbumViewModel
import com.example.picturegallery.feature.change_album_cover.ChangeAlbumCoverViewModel
import com.example.picturegallery.feature.choose_album.ChooseAlbumViewModel
import com.example.picturegallery.feature.create_album.CreateAlbumViewModel
import com.example.picturegallery.feature.photos.PhotosViewModel
import com.example.picturegallery.feature.photos.choose_add_photo_type.AddPhotoAlbumTypeViewModel
import com.example.picturegallery.feature.signin.SignInViewModel
import com.example.picturegallery.injection.annotation.ViewModelKey
import com.example.picturegallery.ui.fragment.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    abstract fun bindSignInViewModel(viewModel: SignInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AlbumViewModel::class)
    abstract fun bindAlbumViewModel(viewModel: AlbumViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PhotosViewModel::class)
    abstract fun bindPhotosViewModel(viewModel: PhotosViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AlbumContentViewModel::class)
    abstract fun bindAlbumContentViewModel(viewModel: AlbumContentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateAlbumViewModel::class)
    abstract fun bindCreateAlbumViewModel(viewModel: CreateAlbumViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangeAlbumCoverViewModel::class)
    abstract fun bindChangeAlbumCoverViewModel(viewModel: ChangeAlbumCoverViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddPhotoAlbumTypeViewModel::class)
    abstract fun bindAddPhotoAlbumTypeViewModel(viewModel: AddPhotoAlbumTypeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChooseAlbumViewModel::class)
    abstract fun bindChooseAlbumViewModel(viewModel: ChooseAlbumViewModel): ViewModel
}