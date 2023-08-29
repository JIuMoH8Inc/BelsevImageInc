package com.example.picturegallery.feature.signin.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.picturegallery.data.data_source.TokenDao
import com.example.picturegallery.data.repository.UserRepositoryImpl
import com.example.picturegallery.domain.model.room.entity.Token
import com.example.picturegallery.feature.signin.action.SignInAction
import com.example.picturegallery.feature.signin.intent.SignInIntent
import com.example.picturegallery.feature.signin.uistate.SignInUiState
import com.example.picturegallery.ui.fragment.base.BaseViewModel
import com.example.picturegallery.utils.flow.SingleFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val tokenDao: TokenDao
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
                    withContext(Dispatchers.IO) {
                        tokenDao.getToken()
                            ?.let {//добавить шифрование токена и написать обработку ошибок от сервера
                                tokenDao.updateToken(Token(token))
                            } ?: run {
                            tokenDao.saveToken(Token(token))
                        }
                    }
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