package com.example.picturegallery.ui.fragment.base
//TODO Добавить удачный баннер и растащить по всем фрагментам, удалить кучу повторов
sealed interface BaseErrorAction {
    data class ShowErrorBanner(val message: String) : BaseErrorAction
    data class ShowSuccessBanner(val message: String) : BaseErrorAction
}