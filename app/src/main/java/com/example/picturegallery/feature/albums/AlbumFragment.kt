package com.example.picturegallery.feature.albums

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.picturegallery.R
import com.example.picturegallery.databinding.ListItemFragmentBinding
import com.example.picturegallery.feature.album_content.AlbumContentFragment
import com.example.picturegallery.feature.albums.adapter.adapter.AlbumAdapter
import com.example.picturegallery.feature.change_album_cover.ChangeAlbumCoverBottomSheet
import com.example.picturegallery.feature.create_album.CreateAlbumFragment
import com.example.picturegallery.ui.dialog.AnswerDialog
import com.example.picturegallery.ui.dialog.InputTwoStringsDialog
import com.example.picturegallery.ui.fragment.base.BaseFragment
import com.example.picturegallery.utils.extensions.observe
import com.faltenreich.skeletonlayout.applySkeleton

class AlbumFragment : BaseFragment<AlbumViewModel>(R.layout.list_item_fragment) {

    private val binding by viewBinding(ListItemFragmentBinding::bind)
    private val albumAdapter by lazy {
        AlbumAdapter(
            { albumId ->
                viewModel.handleIntent(
                    AlbumsFragmentIntent.OnAlbumClick(albumId)
                )
            }
        ) { itemId, albumId ->
            viewModel.handleIntent(
                AlbumsFragmentIntent.OnMoreItemClick(itemId, albumId)
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeLiveData()
        setFragmentListener()
        viewModel.handleIntent(AlbumsFragmentIntent.OnLoadAlbums)
    }

    private fun initViews() = with(binding) {
        itemList.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = albumAdapter
        }
        fabAdd.setOnClickListener {
            viewModel.handleIntent(
                AlbumsFragmentIntent.OnAddAlbumClick
            )
        }
    }

    private fun observeLiveData() {

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            setUiState(uiState)
        }

        viewModel.uiAction.observe(viewLifecycleOwner) { action ->
            handleAction(action)
        }
    }

    private fun setUiState(uiState: AlbumsFragmentUiState) = with(binding) {
        setActionBarTitle(uiState.toolbarTitle)
        albumAdapter.submitList(uiState.albumList)
        showSkeletonLoading(uiState.isLoading)

        uiState.emptyUi?.let { state ->

            itemList.isVisible = false
            fabAdd.isVisible = false

            with(emptyItem) {
                setVisible(true)
                setResUiState(state)
            }

        } ?: run {
            emptyItem.setVisible(false)
            itemList.isVisible = true
            fabAdd.isVisible = true
        }

    }

    private fun handleAction(action: AlbumsFragmentAction) {
        when (action) {
            is AlbumsFragmentAction.OpenAlbum -> {
                navigate(
                    AlbumContentFragment.route(action.id, action.name)
                )
            }

            is AlbumsFragmentAction.OpenDeleteConfirmDialog -> {
                AnswerDialog(action.data).show(childFragmentManager, null)
            }

            AlbumsFragmentAction.OpenCreateAlbumFragment -> {
                navigate(CreateAlbumFragment.route())
            }

            is AlbumsFragmentAction.OpenChangeTitleAndDescDialog -> {
                InputTwoStringsDialog(action.data).show(childFragmentManager, null)
            }

            is AlbumsFragmentAction.OpenChangeCoverDialog -> {
                navigate(
                    ChangeAlbumCoverBottomSheet.route(action.albumId)
                )
            }
        }
    }

    private fun setFragmentListener() {
        setFragmentResultListener(
            ChangeAlbumCoverBottomSheet.CHOSEN_PHOTO_RESULT
        ) { _, result ->
            viewModel.handleIntent(
                AlbumsFragmentIntent.OnChoosePhoto(result.getInt(ChangeAlbumCoverBottomSheet.CHOSEN_PHOTO_ID))
            )
        }

        setAnswerResultListener { result ->
            viewModel.handleIntent(
                AlbumsFragmentIntent.OnHandleAnswerResult(result)
            )
        }

        setInputDialogListener { (first, second) ->
            viewModel.handleIntent(
                AlbumsFragmentIntent.OnHandleInputResult(first, second)
            )
        }
    }

    override fun initRecyclerSkeleton() = binding.itemList.applySkeleton(R.layout.album_card, 4)

    override fun getViewModelClass() = AlbumViewModel::class.java
}