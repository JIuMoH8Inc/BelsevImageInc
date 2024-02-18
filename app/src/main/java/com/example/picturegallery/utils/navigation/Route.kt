package com.example.picturegallery.utils.navigation

import android.os.Bundle
import androidx.annotation.IdRes

data class Route(
    @IdRes val destId: Int,
    val args: Bundle? = null
) {
    companion object {
        const val IS_BOTTOM_NAV_VISIBLE = "IS_BOTTOM_NAV_VISIBLE"
    }
}


