package com.example.picturegallery.ui.fragment.base

import androidx.lifecycle.ViewModel
import com.example.picturegallery.R
import com.example.picturegallery.application.MyApp
import com.example.picturegallery.domain.exception.InvalidLoginPasswordException
import com.example.picturegallery.domain.exception.NoInternetException
import com.example.picturegallery.domain.exception.UnknownErrorException
import com.example.picturegallery.domain.manager.ResourceManager
import com.example.picturegallery.utils.flow.SingleFlow
import javax.inject.Inject

open class BaseViewModel : ViewModel() {

    @Inject
    lateinit var resourceManager: ResourceManager

    private val _uiAction = SingleFlow<BaseErrorAction>()
    val baseUiAction = _uiAction.asSharedFlow()

    init {
        MyApp.instance.appComponent.inject(this)
    }

    protected fun parseError(error: Throwable) {
        when (error) {
            is NoInternetException -> showErrorBanner(resourceManager.getString(R.string.no_internet_error))
            is InvalidLoginPasswordException -> showErrorBanner(resourceManager.getString(R.string.invalid_login_password_error))
            is UnknownErrorException -> showErrorBanner(resourceManager.getString(R.string.unknown_error_text))
        }
    }

    protected fun showErrorBanner(message: String) {
        _uiAction.tryEmit(
            BaseErrorAction.ShowErrorBanner(message)
        )
    }

    protected fun showSuccessBanner(message: String) {
        _uiAction.tryEmit(
            BaseErrorAction.ShowSuccessBanner(message)
        )
    }
}