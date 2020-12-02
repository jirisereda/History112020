package com.history.core.screen

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import com.history.core.events.ErrorEvent
import com.history.core.events.Event
import com.history.core.events.setupDefaultEventHandler

abstract class Screen<
        SM : ScreenViewModel<*, ScreenArgs>, B : ViewDataBinding, ScreenArgs : NavArgs
        > : Fragment() {
    abstract val viewModel: SM
    abstract val layoutId: Int
    abstract val screenToolbar: Toolbar?
    abstract val dataBindViewModel: B.(SM) -> Unit
    abstract val screenArgs: ScreenArgs

    protected lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setArguments(screenArgs)
        setupDefaultEventHandler(viewModel, ::handleEvent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screenToolbar?.let {
            setupToolbarWithNavLibrary(it)
            it.title = findNavController().currentDestination?.label
        }

        setupViews()
    }

    private fun handleError(errorEvent: ErrorEvent) {
        AlertDialog.Builder(requireContext())
            .setTitle(errorEvent.title)
            .setMessage(errorEvent.message)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    open fun setupViews() {}

    open fun handleEvent(event: Event) {}
}