package com.example.picturegallery.utils.extensions

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

inline fun <reified T> CoroutineScope.launchRequest(
    crossinline request: suspend () -> T,
    crossinline onLoading: (Boolean) -> Unit = {},
    crossinline onSuccess: (T) -> Unit = {},
    crossinline onError: (Throwable) -> Unit = {}
) {
    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        launch(Dispatchers.Main) {
            onLoading(false)
            onError(exception)
        }
    }

    val execution = launch(exceptionHandler + SupervisorJob()) {
        onLoading(true)
        request().let { response ->
            onLoading(false)
            onSuccess(response)
        }
    }
}