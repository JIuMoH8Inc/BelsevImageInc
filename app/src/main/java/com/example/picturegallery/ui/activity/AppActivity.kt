package com.example.picturegallery.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.picturegallery.R
import com.example.picturegallery.databinding.AppActivityLayoutBinding
import com.example.picturegallery.ui.listener.ActionBarListener

class AppActivity : AppCompatActivity(), ActionBarListener {

    private lateinit var binding: AppActivityLayoutBinding
    private lateinit var navController: NavController

    private var onBackButtonClick: () -> Unit = {}

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

    override fun setActionBarTitle(titleRes: Int) {
        supportActionBar?.apply {
            setTitle(titleRes)
            show()
        }
    }

    override fun setBackButtonVisibility(isShow: Boolean) {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(isShow)
        }
    }

    override fun setOnBackButtonClickListener(onClick: () -> Unit) {
        onBackButtonClick = onClick
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackButtonClick()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }
}