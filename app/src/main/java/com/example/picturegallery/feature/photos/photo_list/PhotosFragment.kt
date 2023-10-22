package com.example.picturegallery.feature.photos.photo_list

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.picturegallery.R
import com.example.picturegallery.databinding.ListItemFragmentBinding
import com.example.picturegallery.feature.pagination.PaginationScrollListener
import com.example.picturegallery.feature.photos.photo_view.PHOTO_ID_KEY
import com.example.picturegallery.ui.fragment.base.BaseFragment
import com.example.picturegallery.utils.extensions.observe
import com.faltenreich.skeletonlayout.applySkeleton

class PhotosFragment: BaseFragment<PhotosViewModel>(R.layout.list_item_fragment) {

    private val binding by viewBinding(ListItemFragmentBinding::bind)

    private val photoAdapter by lazy {
        PhotosAdapter { id ->
            viewModel.handleIntent(
                PhotoFragmentIntent.OnPhotoClick(id)
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeFlow()
        viewModel.handleIntent(PhotoFragmentIntent.OnLoadPhotoList(true, 0))
    }
    private fun initViews() = with(binding) {
        setBackButtonVisibility(false)
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        itemList.apply {
            layoutManager = gridLayoutManager
            adapter = photoAdapter
        }

        fabAdd.setOnClickListener {

        }
    }

    private fun observeFlow() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            setUiState(uiState)
        }

        viewModel.uiAction.observe(viewLifecycleOwner) { uiAction ->
            handleAction(uiAction)
        }
    }

    private fun setUiState(uiState: PhotosUiState) = with(binding) {
        setActionBarTitle(uiState.toolbarTitle)
        photoAdapter.submitList(uiState.photoList)
        showSkeletonLoading(uiState.isLoading)

        pagingLoadingItem.isVisible = uiState.isNextPageLoading
        pagingLoadBack.isVisible = uiState.isNextPageLoading

        photoAdapter.registerAdapterDataObserver(
            object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    if (!uiState.isLastPage)
                        itemList.smoothScrollToPosition(positionStart)
                }
            }
        )

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

    override fun initRecyclerSkeleton() = binding.itemList.applySkeleton(R.layout.photo_item, 6)

    private fun handleAction(action: PhotoUiAction) {
        when (action) {
            is PhotoUiAction.OpenViewPhotoFragment -> {
                findNavController().navigate(
                    R.id.action_photosFragment_to_viewPhotoFragment,
                    bundleOf(PHOTO_ID_KEY to action.id)
                )
            }
        }
    }

    override fun getViewModelClass() = PhotosViewModel::class.java
}