package com.example.picturegallery.feature.signin.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.picturegallery.data.repository.UserRepositoryImpl
import com.example.picturegallery.domain.manager.CryptoManager
import com.example.picturegallery.feature.signin.action.SignInAction
import com.example.picturegallery.feature.signin.intent.SignInIntent
import com.example.picturegallery.feature.signin.uistate.SignInUiState
import com.example.picturegallery.ui.fragment.base.BaseViewModel
import com.example.picturegallery.utils.flow.SingleFlow
import com.example.picturegallery.utils.manager.AppPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val prefs: AppPreferences,
    private val cryptoManager: CryptoManager
) : BaseViewModel() {

    //"test2", "test2@Asd.s"

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiAction = SingleFlow<SignInAction>()
    val uiAction = _uiAction.asSharedFlow()

    private var login = ""
    private var password = ""

    init {
        _uiState.update {
            it.copy(
                login = "",
                password = ""
            )
        }
    }


    private fun signIn() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            kotlin.runCatching {
                userRepositoryImpl.signIn(login, password)
            }
                .onSuccess { token ->
                    _uiAction.tryEmit(SignInAction.OpenAlbumFragment)
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    prefs.token = cryptoManager.encrypt(CryptoManager.Key.TOKEN, token) ?: ""
                }
                .onFailure {
                    parseError(it)
                    _uiState.update { oldValue ->
                        oldValue.copy(
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun handleIntent(intent: SignInIntent) {
        when (intent) {
            is SignInIntent.OnChangeLogin -> changeLogin(intent.login)
            is SignInIntent.OnChangePassword -> changePassword(intent.password)
            SignInIntent.OnSignInButtonClick -> signIn()
        }
    }

    private fun changeLogin(login: String) {
        _uiState.update {
            it.copy(
                login = login
            )
        }
        this.login = login
    }

    private fun changePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password
            )
        }
        this.password = password
    }

}