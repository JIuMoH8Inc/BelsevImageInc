package com.example.picturegallery.feature.album_content

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.picturegallery.R
import com.example.picturegallery.databinding.ListItemFragmentBinding
import com.example.picturegallery.feature.photos.PhotosFragment
import com.example.picturegallery.feature.photos.adapter.adapter.PhotosAdapter
import com.example.picturegallery.ui.dialog.AnswerDialog
import com.example.picturegallery.ui.fragment.base.BaseFragment
import com.example.picturegallery.utils.extensions.observe
import com.example.picturegallery.utils.navigation.Route
import com.faltenreich.skeletonlayout.applySkeleton

class AlbumContentFragment : BaseFragment<AlbumContentViewModel>(R.layout.list_item_fragment) {
    private val binding by viewBinding(ListItemFragmentBinding::bind)
    private val args by navArgs<AlbumContentFragmentArgs>()

    private val photoAdapter by lazy {
        PhotosAdapter(
            {
                viewModel.handleIntent(
                    AlbumContentIntent.OnPhotoClick(it)
                )
            }
        ) { id, isSelected ->
            viewModel.handleIntent(
                AlbumContentIntent.OnSelectPhoto(id, isSelected)
            )
        }
    }

    private var isVisibleMenu = false

    private val menuProvider = object : MenuProvider {

        override fun onPrepareMenu(menu: Menu) {
            super.onPrepareMenu(menu)
            menu.findItem(R.id.delete_menu_item).isVisible = isVisibleMenu
            menu.findItem(R.id.to_album).isVisible = false
        }

        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.photo_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.delete_menu_item -> {
                    viewModel.handleIntent(
                        AlbumContentIntent.OnDeleteMenuClick
                    )
                    true
                }

                else -> false
            }
        }

    }

    override fun onResume() {
        super.onResume()
        setMenuProvider(menuProvider)
        invalidateMenu()
    }

    override fun onPause() {
        super.onPause()
        removeMenuProvider(menuProvider)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeFlow()
        viewModel.handleIntent(
            AlbumContentIntent.OnParseArguments(args.albumId, args.albumName)
        )
        setFragmentResultListener()
    }

    private fun initViews() = with(binding) {
        with(itemList) {
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = photoAdapter
        }
        fabAdd.setOnClickListener {
            viewModel.handleIntent(
                AlbumContentIntent.OnAddPhotoToAlbumClick
            )
        }
    }

    private fun observeFlow() = with(viewModel) {
        uiState.observe(viewLifecycleOwner, result = ::setUiState)
        uiAction.observe(viewLifecycleOwner, result = ::handleAction)
    }

    private fun setUiState(state: AlbumContentUiState) = with(binding) {
        showBackButton(true)
        setActionBarTitle(state.toolbarTitle)
        showSkeletonLoading(state.isLoading)
        photoAdapter.submitList(state.photoList)
    }

    private fun handleAction(action: AlbumContentUiAction) {
        when (action) {
            is AlbumContentUiAction.SelectPhotos -> {
                PhotosAdapter.isSelectionMode = action.isSelectionMode
                photoAdapter.submitList(action.list)
                setMenuVisible(action.isSelectionMode)
                setActionBarTitle(action.toolbarTitle)
            }

            is AlbumContentUiAction.ShowAnswerDialog -> {
                AnswerDialog(action.dialogData).show(childFragmentManager, null)
            }

            AlbumContentUiAction.OpenChoosePhotoFragment -> {
                navigate(
                    PhotosFragment.route(true)
                )
            }
        }
    }

    private fun setFragmentResultListener() {
        setAnswerResultListener { result ->
            viewModel.handleIntent(AlbumContentIntent.OnAnswerHandle(result))
        }

        setFragmentResultListener(PhotosFragment.PHOTO_IDS) { _, result ->
            val ids = result.getIntegerArrayList(PhotosFragment.PHOTO_IDS_VALUE)
            viewModel.handleIntent(
                AlbumContentIntent.OnAddAlbumList(ids?.toList() ?: emptyList())
            )
        }
    }

    private fun setMenuVisible(isVisible: Boolean) {
        isVisibleMenu = isVisible
        invalidateMenu()
    }

    override fun getViewModelClass() = AlbumContentViewModel::class.java
    override fun initRecyclerSkeleton() = binding.itemList.applySkeleton(R.layout.photo_item, 6)

    companion object {
        fun route(
            albumId: Int,
            albumName: String
        ): Route {
            val args = AlbumContentFragmentArgs(
                albumId,
                albumName
            ).toBundle()
            args.putBoolean(Route.IS_BOTTOM_NAV_VISIBLE, false)
            return Route(
                R.id.albumContentFragment,
                args
            )
        }
    }
}