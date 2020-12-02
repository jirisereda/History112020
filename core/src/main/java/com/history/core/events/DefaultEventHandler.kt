package com.history.core.events

import android.app.AlertDialog
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.history.core.screen.ScreenViewModel
import com.history.core.events.*

/**
 * Sets up default event handler for View Model events
 */
fun <SM : ScreenViewModel<*, *>> Fragment.setupDefaultEventHandler(
    viewModel: SM, otherEventsHandler: (Event) -> Unit
) {
    parentFragmentManager.registerFragmentLifecycleCallbacks(
        object : FragmentManager.FragmentLifecycleCallbacks() {

            override fun onFragmentCreated(
                fragmentManager: FragmentManager,
                fragment: Fragment,
                savedInstanceState: Bundle?
            ) {
                super.onFragmentCreated(fragmentManager, fragment, savedInstanceState)

                if (fragment != this@setupDefaultEventHandler) {
                    return
                }

                viewModel.events.observe(fragment) { event ->
                    when (event) {
                        is NavigationEvent -> findNavController().navigate(event.action, event.args)
                        is OpenUrlEvent -> startActivity(Intent(ACTION_VIEW, Uri.parse(event.url)))
                        is BackEvent -> findNavController().popBackStack()
                        is ErrorEvent -> handleError(event)
                        else -> otherEventsHandler(event)
                    }
                }
            }

            override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                super.onFragmentDestroyed(fm, f)
                if (f == this@setupDefaultEventHandler) {
                    parentFragmentManager.unregisterFragmentLifecycleCallbacks(this)
                }
            }

            private fun handleError(errorEvent: ErrorEvent) {
                AlertDialog.Builder(requireContext())
                    .setTitle(errorEvent.title)
                    .setMessage(errorEvent.message)
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
            }
        },
        false
    )
}