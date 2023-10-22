package com.example.picturegallery.ui.listener

interface ActionBarListener {
    fun hideActionBar()
    fun showActionBar()
    fun setActionBarTitle(title: String)

    fun setActionBarTitle(titleRes: Int)
    fun setBackButtonVisibility(isShow: Boolean)

    fun setOnBackButtonClickListener(onClick: () -> Unit)
}