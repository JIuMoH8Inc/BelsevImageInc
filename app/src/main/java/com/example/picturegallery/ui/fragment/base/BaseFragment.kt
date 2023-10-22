package com.example.picturegallery.ui.fragment.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.picturegallery.application.MyApp
import com.example.picturegallery.ui.activity.AppActivity
import com.example.picturegallery.ui.listener.ActionBarListener
import com.example.picturegallery.utils.BannerManager
import com.example.picturegallery.utils.extensions.observe

abstract class BaseFragment<VM : BaseViewModel>(@LayoutRes layout: Int) : Fragment(layout) {

    lateinit var viewModel: VM

    abstract fun getViewModelClass(): Class<VM>
    private lateinit var listener: ActionBarListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AppActivity) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            MyApp.instance.getViewModelFactory()
        )[getViewModelClass()]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeMessageLiveData()
        setBackClickListener()
    }


    private fun observeMessageLiveData() {
        viewModel.baseUiAction.observe(viewLifecycleOwner) { action ->
            handleAction(action)
        }
    }

    protected fun setActionBarTitle(title: String) {
        listener.setActionBarTitle(title)
    }

    protected fun setActionBarTitle(titleRes: Int) {
        listener.setActionBarTitle(titleRes)
    }

    protected fun showActionBar() {
        listener.showActionBar()
    }

    protected fun hideActionBar() {
        listener.hideActionBar()
    }

    protected fun setBackButtonVisibility(isShow: Boolean) {
        listener.setBackButtonVisibility(isShow)
    }

    private fun setBackClickListener() {
        listener.setOnBackButtonClickListener {
            findNavController().popBackStack()
        }
    }

    private fun handleAction(action: BaseErrorAction) {
        when (action) {
            is BaseErrorAction.ShowErrorBanner -> {
                BannerManager.showErrorBanner(
                    activity = requireActivity(),
                    message = action.message
                )
            }
        }
    }
}