package com.example.picturegallery.feature.albums.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.picturegallery.R
import com.example.picturegallery.databinding.AlbumDetailsBottomsheetBinding
import com.example.picturegallery.feature.albums.uistate.AlbumAdapterUiState
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AlbumDetailsBottomSheet(private val onDeleteClick: () -> Unit) :
    BottomSheetDialogFragment(R.layout.album_details_bottomsheet) {
    private val binding by viewBinding(AlbumDetailsBottomsheetBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.peekHeight = (resources.displayMetrics.heightPixels * 0.5f).toInt()
        return dialog
    }

    private fun initViews() = with(binding) {
        val album = arguments?.getParcelable<AlbumAdapterUiState>(ALBUM_INFO_KEY)!!
        albumName.text = album.albumName
        albumDescription.text = album.albumDescription
        albumItemCount.text = album.itemCount.toString()
        deleteAlbum.text = "Delete"
        deleteAlbum.setOnClickListener {
            onDeleteClick()
            dismiss()
        }
    }

    companion object {
        private val TAG: String = AlbumDetailsBottomSheet::class.java.simpleName

        private const val ALBUM_INFO_KEY = "ALBUM_INFO_KEY"

        fun newInstance(albumInfo: AlbumAdapterUiState, onDeleteClick: () -> Unit) = AlbumDetailsBottomSheet(onDeleteClick).apply {
            arguments = bundleOf(ALBUM_INFO_KEY to albumInfo)
        }
    }

}