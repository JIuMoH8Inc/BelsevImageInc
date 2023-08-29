package com.example.picturegallery.domain.manager

import android.support.annotation.StringRes

interface ResourceManager {
    fun getString(@StringRes id: Int): String
}