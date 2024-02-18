package com.example.picturegallery.feature.choose_album

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.picturegallery.R
import com.example.picturegallery.databinding.ChooseAlbumBottomSheetBinding
import com.example.picturegallery.feature.album_content.AlbumContentFragment
import com.example.picturegallery.feature.albums.adapter.adapter.AlbumAdapter
import com.example.picturegallery.ui.fragment.base.BaseBottomSheetFragment
import com.example.picturegallery.utils.extensions.observe
import com.example.picturegallery.utils.navigation.Route
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class ChooseAlbumBottomSheet :
    BaseBottomSheetFragment<ChooseAlbumViewModel>(R.layout.choose_album_bottom_sheet) {
    private val binding by viewBinding(ChooseAlbumBottomSheetBinding::bind)
    private val args by navArgs<ChooseAlbumBottomSheetArgs>()
    private val albumAdapter by lazy {
        AlbumAdapter(
            { id ->
                viewModel.handleIntent(
                    ChooseAlbumIntent.OnAlbumClick(id)
                )
            }
        ) { _, _ ->

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeFlow()
        viewModel.handleIntent(
            ChooseAlbumIntent.OnParseArgs(args.photoList)
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
        albumList.adapter = albumAdapter
    }

    private fun observeFlow() = with(viewModel) {
        uiState.observe(viewLifecycleOwner, result = ::setUiState)
        uiAction.observe(viewLifecycleOwner, result = ::handleAction)
    }

    private fun setUiState(state: ChooseAlbumUiState) = with(binding) {
        albumAdapter.submitList(state.albumList)
        showSkeletonLoading(state.isLoading)
    }

    private fun handleAction(action: ChooseAlbumUiAction) {
        when (action) {
            is ChooseAlbumUiAction.OpenAlbumContent -> {
                navigate(
                    AlbumContentFragment.route(action.id, action.name),
                    navOptions {
                        popUpTo(R.id.albumFragment)
                    }
                )
            }

            ChooseAlbumUiAction.DismissDialog -> {
                dismiss()
            }
        }
    }

    override fun getViewModelClass() = ChooseAlbumViewModel::class.java
    override fun initRecyclerSkeleton() = binding.albumList.applySkeleton(R.layout.album_card, 4)

    companion object {
        fun route(photoList: IntArray) = Route(
            R.id.chooseAlbumBottomSheet,
            ChooseAlbumBottomSheetArgs(
                photoList = photoList
            ).toBundle()
        )
    }
}