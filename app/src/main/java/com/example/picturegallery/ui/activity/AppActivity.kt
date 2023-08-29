package com.example.picturegallery.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.picturegallery.R
import com.example.picturegallery.databinding.AppActivityLayoutBinding
import com.example.picturegallery.ui.listener.ActionBarListener

class AppActivity : AppCompatActivity(), ActionBarListener {

    private lateinit var binding: AppActivityLayoutBinding
    private lateinit var navController: NavController


    lateinit var test: androidx.core.splashscreen.SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        test = installSplashScreen()
        test.setKeepOnScreenCondition { true }

        binding = AppActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController


        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        navGraph.setStartDestination(R.id.splashFlowFragment)

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
}