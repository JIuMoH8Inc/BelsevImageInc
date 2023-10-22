package com.example.picturegallery.injection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.picturegallery.feature.albums.viewmodel.AlbumViewModel
import com.example.picturegallery.feature.photos.photo_list.PhotosViewModel
import com.example.picturegallery.feature.photos.photo_view.ViewPhotoViewModel
import com.example.picturegallery.feature.signin.viewmodel.SignInViewModel
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
    abstract fun bindPhotosViewModel(viewModel: PhotosViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ViewPhotoViewModel::class)
    abstract fun bindViewPhotoViewModel(viewModel: ViewPhotoViewModel) : ViewModel
}