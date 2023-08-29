package com.example.picturegallery.feature.photos.fragment

import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.picturegallery.R
import com.example.picturegallery.databinding.PhotosFragmentBinding
import com.example.picturegallery.feature.photos.adapter.adapter.PhotosAdapter
import com.example.picturegallery.feature.photos.intent.PhotoFragmentIntent
import com.example.picturegallery.feature.photos.uistate.PhotosUiState
import com.example.picturegallery.feature.photos.viewmodel.PhotosViewModel
import com.example.picturegallery.ui.fragment.base.BaseFragment
import com.example.picturegallery.utils.extensions.observe
import com.example.picturegallery.utils.extensions.setLoadingState
import com.example.picturegallery.utils.fab.AppearingFabBehaviour
import com.faltenreich.skeletonlayout.applySkeleton

class PhotosFragment: BaseFragment<PhotosViewModel>(R.layout.photos_fragment) {

    private val binding by viewBinding(PhotosFragmentBinding::bind)

    private val photoAdapter by lazy {
        PhotosAdapter()
    }

    private val skeleton by lazy {
        binding.photoList.applySkeleton(R.layout.photo_item, 6)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeFlow()
        viewModel.handleIntent(PhotoFragmentIntent.OnLoadPhotoList)
    }

    private fun initViews() = with(binding) {
        photoList.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = photoAdapter
        }

        fabAdd.setOnClickListener {

        }
    }

    private fun observeFlow() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            setUiState(uiState)
        }
    }

    private fun setUiState(uiState: PhotosUiState) = with(binding) {
        setActionBarTitle(uiState.toolbarTitle)
        photoAdapter.submitList(uiState.photoList)
        skeleton.setLoadingState(uiState.isLoading)
    }

    override fun getViewModelClass() = PhotosViewModel::class.java
}