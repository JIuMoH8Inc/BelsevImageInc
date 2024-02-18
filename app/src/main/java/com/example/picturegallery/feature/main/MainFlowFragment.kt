package com.example.picturegallery.feature.main

import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.picturegallery.R
import com.example.picturegallery.databinding.MainFlowFragmentBinding
import com.example.picturegallery.ui.fragment.base.BaseFlowFragment
import com.example.picturegallery.utils.navigation.Route

class MainFlowFragment :
    BaseFlowFragment(R.layout.main_flow_fragment, R.id.nav_host_fragment_main) {
    private val binding by viewBinding(MainFlowFragmentBinding::bind)


    override fun setupNavigation(navController: NavController) {
        with(binding.bottomNavigation) {
            setupWithNavController(navController)
            navController.addOnDestinationChangedListener { _, _, bundle ->
                val isBottomNavVisible =
                    bundle?.getBoolean(Route.IS_BOTTOM_NAV_VISIBLE, true) ?: true
                isVisible = isBottomNavVisible
            }
        }
    }

    companion object {
        fun route() = Route(R.id.mainFlowFragment)
    }
}