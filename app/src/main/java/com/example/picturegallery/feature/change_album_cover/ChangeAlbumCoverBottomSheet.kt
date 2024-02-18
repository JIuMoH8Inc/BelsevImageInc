package com.example.picturegallery.feature.change_album_cover

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.picturegallery.R
import com.example.picturegallery.databinding.ChooseCoverPhotoLayoutBinding
import com.example.picturegallery.feature.photos.adapter.PhotosAdapter
import com.example.picturegallery.ui.fragment.base.BaseBottomSheetFragment
import com.example.picturegallery.utils.extensions.observe
import com.example.picturegallery.utils.navigation.Route
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class ChangeAlbumCoverBottomSheet :
    BaseBottomSheetFragment<ChangeAlbumCoverViewModel>(R.layout.choose_cover_photo_layout) {
    private val binding by viewBinding(ChooseCoverPhotoLayoutBinding::bind)
    private val args by navArgs<ChangeAlbumCoverBottomSheetArgs>()

    private val photoAdapter by lazy {
        PhotosAdapter(
            { id ->
                viewModel.handleIntent(ChangeAlbumCoverIntent.OnPhotoClick(id))
            }
        ) { _, _ ->

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeFlow()
        viewModel.handleIntent(
            ChangeAlbumCoverIntent.OnParseArgs(args.albumId)
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
        }
        return dialog
    }

    private fun initViews() = with(binding) {
        photoList.adapter = photoAdapter
    }

    private fun observeFlow() = with(viewModel) {
        uiState.observe(viewLifecycleOwner, result = ::setUiState)
        uiAction.observe(viewLifecycleOwner, result = ::handleAction)
    }

    private fun setUiState(state: ChangeAlbumCoverUiState) = with(binding) {
        photoAdapter.submitList(state.photoList)
        showSkeletonLoading(state.isLoading)
    }

    private fun handleAction(action: ChangeAlbumCoverUiAction) {
        when (action) {
            is ChangeAlbumCoverUiAction.OnPhotoChoose -> {
                parentFragmentManager.setFragmentResult(
                    CHOSEN_PHOTO_RESULT,
                    bundleOf(CHOSEN_PHOTO_ID to action.id)
                )
                dismiss()
            }
        }
    }

    override fun initRecyclerSkeleton() =
        binding.photoList.applySkeleton(R.layout.photo_item, 8)


    override fun getViewModelClass() = ChangeAlbumCoverViewModel::class.java

    companion object {
        const val CHOSEN_PHOTO_RESULT = "CHOSEN_PHOTO_RESULT"
        const val CHOSEN_PHOTO_ID = "CHOSEN_PHOTO_ID"

        fun route(albumId: Int) = Route(
            R.id.changeAlbumCoverBottomSheet,
            ChangeAlbumCoverBottomSheetArgs(
                albumId = albumId
            ).toBundle()
        )
    }
}