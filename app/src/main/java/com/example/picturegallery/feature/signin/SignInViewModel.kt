package com.example.picturegallery.feature.signin

import androidx.lifecycle.viewModelScope
import com.example.picturegallery.domain.manager.CryptoManager
import com.example.picturegallery.domain.repository.UserRepository
import com.example.picturegallery.ui.fragment.base.BaseViewModel
import com.example.picturegallery.utils.extensions.launchRequest
import com.example.picturegallery.utils.flow.SingleFlow
import com.example.picturegallery.utils.manager.AppPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository,
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

    private fun signIn() = viewModelScope.launchRequest(
        request = {
            userRepository.signIn(login, password)
        },
        onLoading = { isLoading ->
            _uiState.update {
                it.copy(
                    isLoading = isLoading
                )
            }
        },
        onError = {
            parseError(it)
        },
        onSuccess = { token ->
            _uiAction.tryEmit(SignInAction.OpenAlbumFragment)
            prefs.token = cryptoManager.encrypt(CryptoManager.Key.TOKEN, token) ?: ""
        }
    )

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