package com.example.picturegallery.feature.photos

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.picturegallery.R
import com.example.picturegallery.databinding.ListItemFragmentBinding
import com.example.picturegallery.feature.pagination.PaginationScrollListener
import com.example.picturegallery.feature.photos.adapter.PhotosAdapter
import com.example.picturegallery.feature.photos.choose_add_photo_type.AddPhotoAlbumTypeBottomSheet
import com.example.picturegallery.feature.photo_view.ViewPhotoFragment
import com.example.picturegallery.feature.photos.adapter.PhotosViewHolder
import com.example.picturegallery.ui.dialog.AnswerDialog
import com.example.picturegallery.ui.fragment.base.BaseFragment
import com.example.picturegallery.utils.extensions.observe
import com.example.picturegallery.utils.navigation.Route
import com.faltenreich.skeletonlayout.applySkeleton

class PhotosFragment : BaseFragment<PhotosViewModel>(R.layout.list_item_fragment) {

    private val binding by viewBinding(ListItemFragmentBinding::bind)
    private val args by navArgs<PhotosFragmentArgs>()

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
            menu.findItem(R.id.to_album).isVisible = isVisibleMenu
        }

        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.photo_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.delete_menu_item -> {
                    viewModel.handleIntent(
                        PhotoFragmentIntent.OnDeleteMenuClick
                    )
                    true
                }

                R.id.to_album -> {
                    viewModel.handleIntent(
                        PhotoFragmentIntent.OnToAlbumMenuClick
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
        viewModel.handleIntent(PhotoFragmentIntent.OnParseArgs(args.isAddToAlbum))
        setAnswerResultListener { result ->
            viewModel.handleIntent(PhotoFragmentIntent.OnAnswerHandle(result))
        }
    }

    override fun onResume() {
        super.onResume()
        setMenuProvider(menuProvider)
    }

    override fun onPause() {
        super.onPause()
        removeMenuProvider(menuProvider)
        PhotosViewHolder.isSelectionMode = false
    }

    private fun initViews() = with(binding) {
        val gridLayoutManager = GridLayoutManager(requireContext(), 4)
        itemList.apply {
            layoutManager = gridLayoutManager
            adapter = photoAdapter
        }

        fabAdd.setOnClickListener {
            viewModel.handleIntent(
                PhotoFragmentIntent.OnAddPhotoButtonClick(
                    requireActivity().activityResultRegistry
                )
            )
        }

        addToAlbum.setOnClickListener {
            viewModel.handleIntent(
                PhotoFragmentIntent.OnAddToAlbumClick
            )
        }
    }

    private fun observeFlow() = with(viewModel) {
        uiState.observe(viewLifecycleOwner, result = ::setUiState)
        uiAction.observe(viewLifecycleOwner, result = ::handleAction)
    }

    private fun setUiState(uiState: PhotosUiState) = with(binding) {
        setActionBarTitle(uiState.toolbarTitle)

        setMenuVisible(uiState.isSelectionMode && !uiState.isAddToAlbum)

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

                    override val isLastPage: Boolean
                        get() = uiState.isLastPage

                    override val isLoading: Boolean
                        get() = uiState.isNextPageLoading

                }
            )
        }

        fabAdd.isVisible = !uiState.isAddToAlbum
        addToAlbum.isVisible = uiState.isAddToAlbum
        PhotosViewHolder.isSelectionMode = uiState.isAddToAlbum
    }

    private fun handleAction(action: PhotosUiAction) {
        when (action) {
            is PhotosUiAction.ShowAnswerDialog -> {
                AnswerDialog(action.dialogData).show(childFragmentManager, null)
            }

            is PhotosUiAction.SelectPhotos -> {
                PhotosViewHolder.isSelectionMode = action.isSelectionMode
                photoAdapter.submitList(action.list)
                setMenuVisible(action.isSelectionMode && !action.isAddToAlbum)
                setActionBarTitle(action.toolbarTitle)
            }

            is PhotosUiAction.AddPhotosToAlbum -> {
                setFragmentResult(
                    PHOTO_IDS,
                    bundleOf(PHOTO_IDS_VALUE to ArrayList(action.selectedIdList))
                )
                popBackStack()
            }

            is PhotosUiAction.OpenChooseAlbumType -> {
                navigate(AddPhotoAlbumTypeBottomSheet.route(action.selectedPhotos))
            }

            is PhotosUiAction.OpenViewPhotoFragment -> {
                navigate(
                    ViewPhotoFragment.route(action.id)
                )
            }

            is PhotosUiAction.SetPhotos -> {
                photoAdapter.submitList(action.photos)
            }

            is PhotosUiAction.ShowSkeleton -> {
                showSkeletonLoading(action.isShow)
            }
        }
    }

    private fun setMenuVisible(isVisible: Boolean) {
        isVisibleMenu = isVisible
        invalidateMenu()
    }

    override fun initRecyclerSkeleton() = binding.itemList.applySkeleton(R.layout.photo_item, 6)

    override fun getViewModelClass() = PhotosViewModel::class.java

    companion object {
        const val PHOTO_IDS = "PHOTO_IDS"
        const val PHOTO_IDS_VALUE = "PHOTO_IDS_VALUE"

        fun route(isAddToAlbum: Boolean): Route {
            val args = PhotosFragmentArgs(
                isAddToAlbum = isAddToAlbum
            ).toBundle()
            args.putBoolean(Route.IS_BOTTOM_NAV_VISIBLE, false)
            return Route(
                R.id.photosFragment,
                args
            )
        }
    }
}