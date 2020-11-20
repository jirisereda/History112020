package com.history.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.viewbinding.ViewBinding

abstract class NavigationActivity<VB : ViewBinding> : AppCompatActivity() {

    abstract val binding : VB

    abstract val graphId: Int

    open val appBarConfiguration: AppBarConfiguration
        get() = AppBarConfiguration(navController.graph)

    val navHostFragment: NavHostFragment
        get() = supportFragmentManager.findFragmentById(R.id.nav_host_default) as NavHostFragment

    val navController: NavController
        get() = navHostFragment.navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        navController.setGraph(graphId)
    }

    override fun onBackPressed() {
        if (navController.graph.startDestination == navController.currentDestination?.id) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}