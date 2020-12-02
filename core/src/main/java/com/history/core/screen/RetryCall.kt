package com.history.core.screen

import android.app.AlertDialog
import com.history.core.events.Event
import com.history.core.events.LiveEvent
import com.history.core.events.RetryEvent
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.*
import timber.log.Timber

interface SupportsRetryCall {
    val retryChannel: BroadcastChannel<Boolean>

    fun retry(shouldRetry: Boolean) {
        retryChannel.offer(shouldRetry)
    }

    fun <T> Flow<T>.retryCallWithParams(onErrorAction: (Throwable, Long) -> Unit): Flow<T> =
        retryWhen { throwable, attempt ->
            Timber.e(throwable)
            onErrorAction(throwable, attempt)
            return@retryWhen retryChannel.asFlow().first()
        }.catch {}

    fun <T> Flow<T>.retryCall(onErrorAction: () -> Unit): Flow<T> =
        retryCallWithParams { _, _ -> onErrorAction() }

    fun <T> Flow<T>.retryCall(events: LiveEvent<Event>): Flow<T> = retryCall {
        events.value = RetryEvent
    }
}

@Suppress("FunctionName")
fun SupportsRetryCall(): SupportsRetryCall = object : SupportsRetryCall {
    override val retryChannel: BroadcastChannel<Boolean> = BroadcastChannel(1)
}

fun <SM> Screen<SM, *, *>.handleRetryEvent(
    event: Event, uiBuilder: (retry: () -> Unit, doNotRetry: () -> Unit) -> Unit
) where SM : ScreenViewModel<*, *>, SM : SupportsRetryCall {
    if ((event is RetryEvent).not()) {
        return
    }

    uiBuilder({ viewModel.retry(true) }, { viewModel.retry(false) })
}

fun <SM> Screen<SM, *, *>.handleRetryEvent(
    event: Event
) where SM : ScreenViewModel<*, *>, SM : SupportsRetryCall {
    handleRetryEvent(event) { retry, doNotRetry ->
        AlertDialog.Builder(requireContext())
            .setTitle("Retry")
            .setMessage("Press ok to retry")
            .setPositiveButton(android.R.string.ok) { _, _ -> retry() }
            .setNegativeButton(android.R.string.cancel) { _, _ -> doNotRetry() }
            .show()
    }
}