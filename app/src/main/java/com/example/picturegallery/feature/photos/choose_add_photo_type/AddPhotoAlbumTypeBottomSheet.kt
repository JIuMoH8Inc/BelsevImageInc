package com.example.picturegallery.feature.photos.choose_add_photo_type

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.picturegallery.R
import com.example.picturegallery.databinding.ChooseAddPhotoAlbumTypeBinding
import com.example.picturegallery.feature.choose_album.ChooseAlbumBottomSheet
import com.example.picturegallery.feature.create_album.CreateAlbumFragment
import com.example.picturegallery.ui.fragment.base.BaseBottomSheetFragment
import com.example.picturegallery.utils.extensions.observe
import com.example.picturegallery.utils.navigation.Route

class AddPhotoAlbumTypeBottomSheet :
    BaseBottomSheetFragment<AddPhotoAlbumTypeViewModel>(R.layout.choose_add_photo_album_type) {
    private val binding by viewBinding(ChooseAddPhotoAlbumTypeBinding::bind)
    private val args by navArgs<AddPhotoAlbumTypeBottomSheetArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeFlow()
        viewModel.handleIntent(
            AddPhotoAlbumTypeIntent.OnParseArgs(args.photoList.toList())
        )
    }

    override fun getTheme() = R.style.NoBackgroundDialogTheme

    private fun initViews() = with(binding) {
        addToExistAlbum.setOnClickListener {
            viewModel.handleIntent(
                AddPhotoAlbumTypeIntent.OnExistAlbumClick
            )
        }

        createNewAlbum.setOnClickListener {
            viewModel.handleIntent(
                AddPhotoAlbumTypeIntent.OnCreateNewAlbumClick
            )
        }

        cancelButton.setOnClickListener {
            dismiss()
        }
    }

    private fun observeFlow() = with(viewModel) {
        uiAction.observe(viewLifecycleOwner, result = ::handleAction)
    }

    private fun handleAction(action: AddPhotoAlbumTypeUiAction) {
        when (action) {
            is AddPhotoAlbumTypeUiAction.OpenCreateNewAlbumClick -> {
                navigate(
                    CreateAlbumFragment.route(action.photoList)
                )
            }

            is AddPhotoAlbumTypeUiAction.OpenExistAlbumsFragment -> {
                navigate(
                    ChooseAlbumBottomSheet.route(action.photoList)
                )
            }
        }
    }

    override fun getViewModelClass() = AddPhotoAlbumTypeViewModel::class.java

    companion object {
        fun route(photoList: IntArray) = Route(
            R.id.addPhotoAlbumTypeBottomSheet,
            AddPhotoAlbumTypeBottomSheetArgs(
                photoList = photoList
            ).toBundle()
        )
    }
}