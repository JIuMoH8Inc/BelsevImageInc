package com.example.picturegallery.feature.photos.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.picturegallery.R
import com.example.picturegallery.databinding.ListItemFragmentBinding
import com.example.picturegallery.feature.pagination.PaginationScrollListener
import com.example.picturegallery.feature.photos.adapter.adapter.PhotosAdapter
import com.example.picturegallery.feature.photos.intent.PhotoFragmentIntent
import com.example.picturegallery.feature.photos.uiAction.PhotosUiAction
import com.example.picturegallery.feature.photos.uistate.PhotosUiState
import com.example.picturegallery.feature.photos.viewmodel.PhotosViewModel
import com.example.picturegallery.ui.dialog.AnswerDialog
import com.example.picturegallery.ui.fragment.base.BaseFragment
import com.example.picturegallery.utils.BannerManager
import com.example.picturegallery.utils.extensions.observe
import com.faltenreich.skeletonlayout.applySkeleton

class PhotosFragment: BaseFragment<PhotosViewModel>(R.layout.list_item_fragment) {

    private val binding by viewBinding(ListItemFragmentBinding::bind)

    private val photoAdapter by lazy {
        PhotosAdapter(
            {
                viewModel.handleIntent(
                    PhotoFragmentIntent.OnPhotoClick(it)
                )
            }
        )
         { id, isSelected ->
            viewModel.handleIntent(
                PhotoFragmentIntent.OnSelectPhoto(id, isSelected)
            )
        }
    }

    private var isVisibleMenu = false

    private val menuProvider = object : MenuProvider {

        override fun onPrepareMenu(menu: Menu) {
            super.onPrepareMenu(menu)
            menu.findItem(R.id.delete_menu_item).isVisible = isVisibleMenu
        }

        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.delete_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.delete_menu_item -> {
                    viewModel.handleIntent(
                        PhotoFragmentIntent.OnDeleteMenuClick
                    )
                    true
                }

                 else -> false
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeFlow()
        viewModel.handleIntent(PhotoFragmentIntent.OnLoadPhotoList(true, 0))
    }

    override fun onResume() {
        super.onResume()
        setMenuProvider(menuProvider)
    }

    override fun onPause() {
        super.onPause()
        removeMenuProvider(menuProvider)
    }
    private fun initViews() = with(binding) {
        val gridLayoutManager = GridLayoutManager(requireContext(), 4)
        itemList.apply {
            layoutManager = gridLayoutManager
            adapter = photoAdapter
        }

        fabAdd.setOnClickListener {

        }
    }

    private fun observeFlow() = with(viewModel) {
        uiState.observe(viewLifecycleOwner, result = ::setUiState)
        uiAction.observe(viewLifecycleOwner, result = ::handleAction)
    }

    private fun setUiState(uiState: PhotosUiState) = with(binding) {
        setActionBarTitle(uiState.toolbarTitle)

        with(photoAdapter) {
            submitList(uiState.photoList)
            registerAdapterDataObserver(
                object : RecyclerView.AdapterDataObserver() {
                    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                        if (!uiState.isLastPage)
                            itemList.smoothScrollToPosition(positionStart)
                    }
                }
            )
        }

        setMenuVisible(uiState.isSelectionMode)

        showSkeletonLoading(uiState.isLoading)

        pagingLoadingItem.isVisible = uiState.isNextPageLoading
        pagingLoadBack.isVisible = uiState.isNextPageLoading

        itemList.apply {
            clearOnScrollListeners()
            addOnScrollListener(
                object : PaginationScrollListener() {
                    override fun needLoadMoreItems(offset: Int) {
                        viewModel.handleIntent(
                            PhotoFragmentIntent.OnLoadPhotoList(false, offset)
                        )
                    }

                    override fun isLastPage(): Boolean = uiState.isLastPage

                    override fun isLoading(): Boolean = uiState.isNextPageLoading

                }
            )
        }
    }

    private fun handleAction(action: PhotosUiAction) {
        when (action) {
            is PhotosUiAction.ShowErrorBanner -> {
                BannerManager.showErrorBanner(
                    activity = requireActivity(),
                    message = action.message
                )
            }

            is PhotosUiAction.ShowSuccessBanner -> {
                BannerManager.showSuccessBanner(
                    activity = requireActivity(),
                    message = action.message
                )
            }

            is PhotosUiAction.ShowAnswerDialog -> {
                AnswerDialog(action.dialogData).show(childFragmentManager, null)
            }

            is PhotosUiAction.SelectPhotos -> {
                PhotosAdapter.isSelectionMode = action.isSelectionMode
                photoAdapter.submitList(action.list)
                setMenuVisible(action.isSelectionMode)
                setActionBarTitle(action.toolbarTitle)
            }
        }
    }

    private fun setMenuVisible(isVisible: Boolean) {
        isVisibleMenu = isVisible
        invalidateMenu()
    }

    override fun initRecyclerSkeleton() = binding.itemList.applySkeleton(R.layout.photo_item, 6)

    override fun getViewModelClass() = PhotosViewModel::class.java
}