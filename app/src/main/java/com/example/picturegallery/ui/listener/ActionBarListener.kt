package com.example.picturegallery.ui.listener

interface ActionBarListener {
    fun hideActionBar()
    fun showActionBar()
    fun setActionBarTitle(title: String)
    fun showBackButton(isShow: Boolean)
}