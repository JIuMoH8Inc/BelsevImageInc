package com.example.picturegallery.feature.photos.photo_view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.picturegallery.R
import com.example.picturegallery.databinding.ViewPhotoFragmentBinding
import com.example.picturegallery.ui.fragment.base.BaseFragment
import com.example.picturegallery.utils.extensions.observe

const val ROTATION_ANGLE = 90f
class ViewPhotoFragment : BaseFragment<ViewPhotoViewModel>(R.layout.view_photo_fragment) {

    private val binding by viewBinding(ViewPhotoFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeFlow()
        viewModel.parseArguments(requireArguments())
        initViews()
    }

    private fun initViews() {
        setBackButtonVisibility(true)
        with(binding) {
            errorItem.errorButton.setOnClickListener {
                viewModel.handleIntent(
                    ViewPhotoIntent.OnTryAgainButtonClick
                )
            }
        }
    }

    private fun observeFlow() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            setUiState(uiState)
        }
    }

    private fun setUiState(uiState: ViewPhotoUiState) = with(binding) {
        photoContainer.apply {
            isVisible = !uiState.isError
            setImageBitmap(uiState.photo)
            if (uiState.isNeedToRotate) rotation = ROTATION_ANGLE
        }
        photoContainer.isVisible = !uiState.isError
        setActionBarTitle(uiState.toolbarTitle)
        photoContainer.setImageBitmap(uiState.photo)
        errorItem.apply {
            root.isVisible = uiState.isError
            errorImage.setImageResource(uiState.errorDrawable)
            errorHeader.setText(uiState.photoErrorHeader)
            errorDesc.isVisible = false
            errorButton.setText(uiState.errorButtonText)
        }
    }
    override fun getViewModelClass() = ViewPhotoViewModel::class.java
}