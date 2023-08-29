package com.example.picturegallery.feature.main

import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.picturegallery.R
import com.example.picturegallery.databinding.MainFlowFragmentBinding
import com.example.picturegallery.ui.fragment.base.BaseFlowFragment
import com.example.picturegallery.utils.extensions.activityNavController

class MainFlowFragment: BaseFlowFragment(R.layout.main_flow_fragment, R.id.nav_host_fragment_main) {
    private val binding by viewBinding(MainFlowFragmentBinding::bind)

    override fun setupNavigation(navController: NavController) {
        binding.bottomNavigation.setupWithNavController(navController)
    }
}