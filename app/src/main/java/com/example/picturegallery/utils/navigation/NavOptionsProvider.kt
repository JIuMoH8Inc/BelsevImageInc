package com.example.picturegallery.utils.navigation

import androidx.navigation.NavOptions
import androidx.navigation.navOptions

object NavOptionsProvider {

    val defaultNavOptions: NavOptions
        get() = navOptions {
            anim {
                enter = android.R.anim.fade_in
                popExit = android.R.anim.fade_out
            }
        }
}