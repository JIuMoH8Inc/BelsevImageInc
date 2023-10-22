package com.example.picturegallery.feature.photos.photo_view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.picturegallery.R
import com.example.picturegallery.databinding.ViewPhotoFragmentBinding
import com.example.picturegallery.ui.fragment.base.BaseFragment
import com.example.picturegallery.utils.extensions.observe
import com.example.picturegallery.utils.navigation.Route

const val ROTATION_ANGLE = 90f
class ViewPhotoFragment : BaseFragment<ViewPhotoViewModel>(R.layout.view_photo_fragment) {

    private val binding by viewBinding(ViewPhotoFragmentBinding::bind)
    private val args by navArgs<ViewPhotoFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeFlow()
        initViews()
        viewModel.handleIntent(
            ViewPhotoIntent.OnParseArgs(args.photoId)
        )
    }

    private fun initViews() = with(binding) {
        showBackButton(true)
    }

    private fun observeFlow() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            setUiState(uiState)
        }
    }

    private fun setUiState(uiState: ViewPhotoUiState) = with(binding) {
        photoContainer.apply {
            setImageBitmap(uiState.photo)
            if (uiState.isNeedToRotate) rotation = ROTATION_ANGLE
        }
        setActionBarTitle(uiState.toolbarTitle)
        photoContainer.setImageBitmap(uiState.photo)
        uiState.errorUi?.let {
            with(errorItem) {
                setVisible(true)
                setResUiState(it)
            }
        } ?: run {
            errorItem.setVisible(false)
        }
    }

    override fun getViewModelClass() = ViewPhotoViewModel::class.java

    companion object {
        fun route(photoId: Int): Route {
            val args = ViewPhotoFragmentArgs(photoId).toBundle()
            args.putBoolean(Route.IS_BOTTOM_NAV_VISIBLE, false)
            return Route(R.id.viewPhotoFragment, args)
        }
    }
}