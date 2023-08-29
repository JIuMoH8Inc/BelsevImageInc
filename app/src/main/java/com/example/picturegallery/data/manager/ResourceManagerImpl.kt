package com.example.picturegallery.data.manager

import android.app.Application
import com.example.picturegallery.domain.manager.ResourceManager
import javax.inject.Inject

class ResourceManagerImpl @Inject constructor(private val context: Application) : ResourceManager {
    override fun getString(id: Int): String = context.getString(id)
}