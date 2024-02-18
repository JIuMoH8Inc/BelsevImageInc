package com.example.picturegallery.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.picturegallery.R
import com.example.picturegallery.databinding.AppActivityLayoutBinding
import com.example.picturegallery.ui.listener.ActionBarListener

class AppActivity : AppCompatActivity(), ActionBarListener {

    private lateinit var binding: AppActivityLayoutBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AppActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController


        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        navGraph.setStartDestination(R.id.signInFlowFragment)

        navController.graph = navGraph

    }

    override fun hideActionBar() {
        supportActionBar?.hide()
    }

    override fun showActionBar() {
        supportActionBar?.show()
    }

    override fun setActionBarTitle(title: String) {
        supportActionBar?.apply {
            this.title = title
            show()
        }
    }

    override fun showBackButton(isShow: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(isShow)
    }

    override fun setMenuProvider(menuProvider: MenuProvider) {
        addMenuProvider(menuProvider)
    }

    override fun removeCustomMenuProvider(menuProvider: MenuProvider) {
        removeMenuProvider(menuProvider)
    }

    override fun invalidateCustomMenu() {
        invalidateMenu()
    }
}