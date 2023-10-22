package com.example.picturegallery.ui.fragment.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.picturegallery.application.MyApp
import com.example.picturegallery.ui.activity.AppActivity
import com.example.picturegallery.ui.dialog.AnswerDialog
import com.example.picturegallery.ui.dialog.InputTwoStringsDialog
import com.example.picturegallery.ui.listener.ActionBarListener
import com.example.picturegallery.utils.BannerManager
import com.example.picturegallery.utils.extensions.activityNavController
import com.example.picturegallery.utils.extensions.getParcelableCompat
import com.example.picturegallery.utils.extensions.observe
import com.example.picturegallery.utils.extensions.setLoadingState
import com.example.picturegallery.utils.navigation.NavOptionsProvider
import com.example.picturegallery.utils.navigation.Route
import com.faltenreich.skeletonlayout.Skeleton

abstract class BaseFragment<VM : BaseViewModel>(@LayoutRes layout: Int) : Fragment(layout) {

    lateinit var viewModel: VM

    private val skeleton: Skeleton by lazy {
        initRecyclerSkeleton()
    }

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
        observeMessageFlow()
        showBackButton(false)
    }

    protected fun navigate(
        route: Route,
        navOptions: NavOptions = NavOptionsProvider.defaultNavOptions
    ) {
        try {
            findNavController().navigate(route.destId, route.args, navOptions)
        } catch (e: Throwable) {
            activityNavController().navigate(route.destId, route.args, navOptions)
        }
    }

    protected fun popBackStack() {
        try {
            findNavController().popBackStack()
        } catch (e: Throwable) {
            activityNavController().popBackStack()
        }
    }

    protected fun navigateBackTo(route: Route) {
        try {
            findNavController().popBackStack(route.destId, inclusive = true)
        } catch (e: Throwable) {
            activityNavController().popBackStack(route.destId, inclusive = true)
        }
    }

    private fun observeMessageFlow() {
        viewModel.baseUiAction.observe(viewLifecycleOwner) { action ->
            handleBaseAction(action)
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

    fun showBackButton(isShow: Boolean) {
        listener.showBackButton(isShow)
    }

    protected fun setMenuProvider(menuProvider: MenuProvider) {
        listener.setMenuProvider(menuProvider)
    }

    protected fun removeMenuProvider(menuProvider: MenuProvider) {
        listener.removeCustomMenuProvider(menuProvider)
    }

    protected fun invalidateMenu() {
        listener.invalidateCustomMenu()
    }

    private fun handleBaseAction(action: BaseErrorAction) {
        when (action) {
            is BaseErrorAction.ShowErrorBanner -> {
                BannerManager.showErrorBanner(
                    activity = requireActivity(),
                    message = action.message
                )
            }

            is BaseErrorAction.ShowSuccessBanner -> {
                BannerManager.showSuccessBanner(
                    activity = requireActivity(),
                    message = action.message
                )
            }
        }
    }

    protected open fun initRecyclerSkeleton(): Skeleton {
        TODO("stub for overriding")
    }

    protected fun showSkeletonLoading(isLoading: Boolean) {
        skeleton.setLoadingState(isLoading)
    }

    protected fun setAnswerResultListener(answer: (Boolean) -> Unit) {
        childFragmentManager.setFragmentResultListener(
            AnswerDialog.ANSWER_DIALOG_RESULT,
            viewLifecycleOwner
        ) { _, result ->
            answer(result.getBoolean(AnswerDialog.ANSWER_DIALOG_RESULT_VALUE))
        }
    }

    protected fun setInputDialogListener(inputs: (InputTwoStringsDialog.InputResult) -> Unit) {
        childFragmentManager.setFragmentResultListener(
            InputTwoStringsDialog.INPUT_DIALOG_RESULT,
            viewLifecycleOwner
        ) { _, result ->
            inputs(
                result.getParcelableCompat<InputTwoStringsDialog.InputResult>(InputTwoStringsDialog.INPUT_RESULT_VALUE)
                    ?: InputTwoStringsDialog.InputResult()
            )
        }
    }
}