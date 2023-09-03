package com.example.picturegallery.feature.albums.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.picturegallery.R
import com.example.picturegallery.databinding.ListItemFragmentBinding
import com.example.picturegallery.feature.albums.action.AlbumsFragmentAction
import com.example.picturegallery.feature.albums.adapter.AlbumAdapter
import com.example.picturegallery.feature.albums.intent.AlbumsFragmentIntent
import com.example.picturegallery.feature.albums.uistate.AlbumsFragmentUiState
import com.example.picturegallery.feature.albums.viewmodel.AlbumViewModel
import com.example.picturegallery.ui.dialog.AnswerDialog
import com.example.picturegallery.ui.fragment.base.BaseFragment
import com.example.picturegallery.utils.extensions.observe
import com.example.picturegallery.utils.extensions.setLoadingState
import com.faltenreich.skeletonlayout.applySkeleton
import com.tapadoo.alerter.Alerter

class AlbumFragment: BaseFragment<AlbumViewModel>(R.layout.list_item_fragment) {

    private val binding by viewBinding(ListItemFragmentBinding::bind)
    private lateinit var albumAdapter: AlbumAdapter

    private val skeleton by lazy {
        binding.itemList.applySkeleton(R.layout.album_item, 4)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeLiveData()
        viewModel.handleIntent(AlbumsFragmentIntent.OnLoadAlbums)
    }

    private fun initViews() {
        albumAdapter = AlbumAdapter(
            { albumId ->
                viewModel.handleIntent(AlbumsFragmentIntent.OnAlbumClick(albumId))
            }
        ) { album ->
            viewModel.handleIntent(AlbumsFragmentIntent.OnMoreClick(album))
        }

        binding.itemList.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = albumAdapter
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
        albumAdapter.setNewAlbumList(uiState.albumList)
        skeleton.setLoadingState(uiState.isLoading)
    }

    private fun handleAction(action: AlbumsFragmentAction) {
        when (action) {
            is AlbumsFragmentAction.OpenAlbum -> {
                Alerter.create(requireActivity())
                    .enableIconPulse(true)
                    .setTitle("Album title")
                    .setText("Test")
                    .setOnClickListener {
                        Toast.makeText(requireContext(), "Click on banner", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .show()
            }

            is AlbumsFragmentAction.OpenAlbumDetailsBottomSheet -> {
                AlbumDetailsBottomSheet.newInstance(action.album) {
                    AnswerDialog(
                        AnswerDialog.DialogData(
                            header = action.album.albumName,
                            answer = "Do you want to delete ${action.album.albumName}",
                            positiveButtonText = "Yes",
                            negativeButtonText = "No",
                            positiveButtonClick = {
                            },
                            negativeButtonClick = {}
                        )
                    ).show(childFragmentManager, null)
                }.show(childFragmentManager, null)
            }
        }
    }

    override fun getViewModelClass() = AlbumViewModel::class.java
}