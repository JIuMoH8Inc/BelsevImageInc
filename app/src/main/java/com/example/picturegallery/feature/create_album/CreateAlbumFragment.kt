package com.example.picturegallery.feature.create_album

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.picturegallery.R
import com.example.picturegallery.databinding.CreateAlbumLayoutBinding
import com.example.picturegallery.feature.album_content.AlbumContentFragment
import com.example.picturegallery.feature.photos.adapter.adapter.PhotosAdapter
import com.example.picturegallery.ui.fragment.base.BaseFragment
import com.example.picturegallery.utils.extensions.observe
import com.example.picturegallery.utils.navigation.Route
import com.faltenreich.skeletonlayout.applySkeleton

class CreateAlbumFragment : BaseFragment<CreateAlbumViewModel>(R.layout.create_album_layout) {
    private val binding by viewBinding(CreateAlbumLayoutBinding::bind)
    private val args by navArgs<CreateAlbumFragmentArgs>()

    private val photosAdapter by lazy {
        PhotosAdapter(
            {

            }
        ) { id, isSelected ->
            viewModel.handleIntent(
                CreateAlbumIntent.OnSelectPhoto(id, isSelected)
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeFlow()
        viewModel.handleIntent(
            CreateAlbumIntent.ParseArgs(args.photoList?.toList())
        )
    }

    override fun onPause() {
        super.onPause()
        PhotosAdapter.isSelectionMode = false
    }

    private fun initViews() = with(binding) {
        PhotosAdapter.isSelectionMode = true
        photoList.adapter = photosAdapter
        showBackButton(true)
        createAlbum.setOnClickListener {
            viewModel.handleIntent(
                CreateAlbumIntent.OnCreateAlbumClick
            )
        }

        albumName.addTextChangedListener {
            viewModel.handleIntent(
                CreateAlbumIntent.OnTitleChange(it.toString())
            )
        }

        albumDesc.addTextChangedListener {
            viewModel.handleIntent(
                CreateAlbumIntent.OnDescChange(it.toString())
            )
        }
    }

    private fun observeFlow() = with(viewModel) {
        uiState.observe(viewLifecycleOwner, result = ::setUiState)
        uiAction.observe(viewLifecycleOwner, result = ::handleAction)
    }

    private fun setUiState(state: CreateAlbumUiState) = with(binding) {
        setActionBarTitle(state.toolbarTitle)
        showSkeletonLoading(state.isLoading)
        photosAdapter.submitList(state.photoList)
        with(state.isEmptyPhotoList) {
            photoError.isVisible = this
            if (this) {
                nestedScrollItem.smoothScrollTo(0, photoHeader.top)
            }
        }
        albumNameInput.error = state.emptyNameError
        createAlbum.isEnabled = !state.isLoading

        state.errorUi?.let { infoState ->
            photoList.isVisible = false
            with(infoView) {
                setVisible(true)
                setResUiState(infoState)
            }
        } ?: run {
            photoList.isVisible = true
            infoView.setVisible(false)
        }

        photoHeader.isVisible = state.isPhotoHeaderVisible
    }

    private fun handleAction(action: CreateAlbumUiAction) {
        when (action) {
            is CreateAlbumUiAction.SelectPhotos -> {
                photosAdapter.submitList(action.photos)
            }

            is CreateAlbumUiAction.SetEmptyNameError -> {
                with(binding) {
                    albumNameInput.error = action.error
                    action.error?.let {
                        nestedScrollItem.smoothScrollTo(0, dataHeader.top)
                    }
                }
            }

            is CreateAlbumUiAction.OpenNewAlbum -> {
                navigate(
                    AlbumContentFragment.route(action.id, action.name),
                    navOptions {
                        popUpTo(R.id.albumFragment)
                    }
                )
            }
        }
    }

    override fun getViewModelClass() = CreateAlbumViewModel::class.java
    override fun initRecyclerSkeleton() = binding.photoList.applySkeleton(R.layout.photo_item, 8)

    companion object {
        fun route() =
            Route(
                R.id.createAlbumFragment,
                bundleOf(Route.IS_BOTTOM_NAV_VISIBLE to false)
            )

        fun route(photoList: IntArray): Route {
            val args = CreateAlbumFragmentArgs(
                photoList
            ).toBundle()
            args.putBoolean(Route.IS_BOTTOM_NAV_VISIBLE, false)
            return Route(R.id.createAlbumFragment, args)
        }
    }
}