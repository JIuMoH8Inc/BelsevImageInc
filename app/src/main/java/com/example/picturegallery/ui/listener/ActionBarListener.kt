package com.example.picturegallery.ui.listener

import androidx.core.view.MenuProvider

interface ActionBarListener {
    fun hideActionBar()
    fun showActionBar()
    fun setActionBarTitle(title: String)
    fun showBackButton(isShow: Boolean)
    fun setMenuProvider(menuProvider: MenuProvider)
    fun removeCustomMenuProvider(menuProvider: MenuProvider)
    fun invalidateCustomMenu()
}