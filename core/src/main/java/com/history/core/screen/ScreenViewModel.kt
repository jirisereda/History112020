package com.history.core.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavArgs
import androidx.navigation.NavDirections
import com.history.core.events.*
import com.history.core.toErrorEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import timber.log.Timber

abstract class ScreenViewModel<State, ScreenArgs : NavArgs>(initialState: State) : ViewModel() {
    private val mutableState = MutableStateFlow(initialState)

    val currentState: State
        get() = mutableState.value

    val state: LiveData<State> = mutableState.asLiveData(viewModelScope.coroutineContext)

    val events = LiveEvent<Event>()

    lateinit var screenArguments: ScreenArgs
        private set

    protected fun State.next(nextState: State.() -> State) {
        mutableState.value = nextState()
    }

    protected fun nextEvent(event: Event) {
        events.value = event
    }

    private fun navigate(navigationEvent: NavigationEvent) = nextEvent(navigationEvent)

    protected fun navigate(navigationAction: Int) = navigate(NavigationEvent(navigationAction))

    protected fun NavDirections.navigate() = nextEvent(toNavEvent())

    fun navigateBack(): Unit = nextEvent(BackEvent)

    fun setArguments(screenArguments: ScreenArgs) {
        if (!this::screenArguments.isInitialized) {
            this.screenArguments = screenArguments
            onArgumentsSet(screenArguments)
        }
    }

    protected open fun onArgumentsSet(screenArguments: ScreenArgs) {}

    protected fun <T> Flow<T>.handleErrors(action: suspend FlowCollector<T>.(Throwable) -> Unit): Flow<T> = catch {
        Timber.e(it)
        action(it)
    }

    protected fun <T> Flow<T>.handleErrors(): Flow<T> = handleErrors {
        nextEvent(it.toErrorEvent())
    }

    protected fun <T> Flow<T>.reportErrors(): Flow<T> = handleErrors { /* Nothing to do */ }
}