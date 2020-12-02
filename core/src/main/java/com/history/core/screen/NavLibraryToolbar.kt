package com.history.core.screen

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.history.core.NavigationActivity

/**
 * Sets up toolbar with navigation library, hacking navigation button so that navigation button from
 * styles is used, instead of the navigation button provided by navigation library
 *
 * Call this method from [Fragment.onViewCreated]
 */
fun Fragment.setupToolbarWithNavLibrary(toolbar: Toolbar) {
    parentFragmentManager.registerFragmentLifecycleCallbacks(
        object : FragmentManager.FragmentLifecycleCallbacks() {
            private var listener: NavController.OnDestinationChangedListener? = null

            override fun onFragmentViewCreated(
                fm: FragmentManager, fragment: Fragment, v: View, savedInstanceState: Bundle?
            ) {
                if (fragment != this@setupToolbarWithNavLibrary) {
                    return
                }

                (fragment.requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
                val styleNavigationIcon = toolbar.navigationIcon
                //todo: there is an mlk in all used views -> must be further investigated how to remove ToolbarOnDestinationChangedListener
                toolbar.setupWithNavController(
                    fragment.findNavController(),
                    (requireActivity() as NavigationActivity<*>).appBarConfiguration
                )

                // Hack alert: This is a hack to apply navigation icon from styles of toolbar
                // Without this hack, Navigation Library overrides the icon and there's no way to
                // specify custom navigation icon as of yet. That's why styleNavigationIcon needs to be
                // stored before calling toolbar::setupWithNavigationController
                listener?.let(fragment.findNavController()::removeOnDestinationChangedListener)
                listener = NavController.OnDestinationChangedListener { _, _, _ ->
                    if (toolbar.navigationIcon != null) {
                        toolbar.navigationIcon = styleNavigationIcon
                    }
                }.also(fragment.findNavController()::addOnDestinationChangedListener)
            }

            override fun onFragmentViewDestroyed(fm: FragmentManager, fragment: Fragment) {
                super.onFragmentViewDestroyed(fm, fragment)

                if (this@setupToolbarWithNavLibrary == fragment) {
                    (fragment.requireActivity() as AppCompatActivity).setSupportActionBar(null)
                    listener?.let(fragment.findNavController()::removeOnDestinationChangedListener)
                    parentFragmentManager.unregisterFragmentLifecycleCallbacks(this)
                }
            }
        },
        false
    )
}