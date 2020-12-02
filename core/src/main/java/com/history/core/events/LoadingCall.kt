package com.history.core.events

import kotlinx.coroutines.flow.*

fun <T> Flow<T>.loadingCall(loadingStart: () -> Unit, loadingFinish: () -> Unit): Flow<T> = this
    .onStart { loadingStart() }
    .onCompletion { loadingFinish() }

fun <T> Flow<T>.loadingCallWithErrors(loadingStart: () -> Unit, loadingFinish: () -> Unit, error: (Throwable) -> Unit): Flow<T> = this
    .onStart { loadingStart() }
    .onEach { loadingFinish() }
    .catch {
        error(it)
        throw it // So that collectors have a chance to do something else with the error
    }