package com.example.picturegallery.feature.signin.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.picturegallery.R
import com.example.picturegallery.databinding.SignInFragmentBinding
import com.example.picturegallery.feature.signin.action.SignInAction
import com.example.picturegallery.feature.signin.intent.SignInIntent
import com.example.picturegallery.feature.signin.uistate.SignInUiState
import com.example.picturegallery.feature.signin.viewmodel.SignInViewModel
import com.example.picturegallery.ui.fragment.base.BaseFragment
import com.example.picturegallery.utils.BannerManager
import com.example.picturegallery.utils.extensions.activityNavController
import com.example.picturegallery.utils.extensions.navigateSafely
import com.example.picturegallery.utils.extensions.observe

class SignInFragment : BaseFragment<SignInViewModel>(R.layout.sign_in_fragment) {

    private val binding by viewBinding(SignInFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeLiveData()
    }

    private fun initViews() = with(binding) {

        hideActionBar()

        loginEditText.addTextChangedListener { login ->
            viewModel.handleIntent(SignInIntent.OnChangeLogin(login.toString().trim()))
        }

        passwordEditText.addTextChangedListener { password ->
            viewModel.handleIntent(SignInIntent.OnChangePassword(password.toString().trim()))
        }

        signInButton.setOnClickListener {
            viewModel.handleIntent(SignInIntent.OnSignInButtonClick)
        }
    }

    private fun observeLiveData() {
        viewModel.uiState.observe(viewLifecycleOwner) {  uiState ->
            setUiState(uiState)
        }

        viewModel.uiAction.observe(viewLifecycleOwner) { action ->
            handleAction(action)
        }
    }

    private fun setUiState(uiState: SignInUiState) = with(binding) {
        signInButton.apply {
            isEnabled(uiState.login.isNotEmpty() and uiState.password.isNotEmpty())
            setLoadingState(uiState.isLoading)
        }
        loginInput.setHint(uiState.loginHint)
        passwordInput.setHint(uiState.passwordHint)
    }

    private fun handleAction(action: SignInAction) {
        when (action) {
            is SignInAction.OpenAlbumFragment -> {
                activityNavController().navigateSafely(R.id.action_signInFlowFragment_to_mainFlowFragment)
            }
        }
    }

    override fun getViewModelClass() = SignInViewModel::class.java

}