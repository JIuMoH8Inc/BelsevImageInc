package com.example.picturegallery.ui.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(
    private val viewModel: MutableMap<Class<out ViewModel>,
            @JvmSuppressWildcards Provider<ViewModel>>

) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModelProvider = viewModel[modelClass]
            ?: throw IllegalArgumentException("View model class $modelClass not found. Add viewModel in NewViewModelModule")

        return viewModelProvider.get() as T
    }
}