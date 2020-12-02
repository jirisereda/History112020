package com.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.history.common.ui.views.LoadingState
import com.history.core.events.EmptyArgs
import com.history.core.screen.ScreenViewModel
import com.history.core.screen.SupportsRetryCall

data class StreamsState(
    val loadingState: LoadingState = LoadingState.Loading,
    val streams: List<StreamItem> = emptyList()
)

class HistoricalEventsViewModel @ViewModelInject constructor(
    private val productContextStorage: SelectedProductContextStorage,
    private val streamsApiService: StreamsApiService
    ) : ScreenViewModel<StreamsState, EmptyArgs>(StreamsState()),
    SupportsRetryCall by SupportsRetryCall() {

    private val shopProductContext: ShopProductContext
        get() = productContextStorage.productContext ?: ShopProductContext.FEMALE

    override fun onArgumentsSet(screenArguments: EmptyArgs) {

        streamsApiService.query(shopProductContext, ApolloResponseFetchers.CACHE_FIRST)
            .loadingCall(
                { currentState.next { copy(loadingState = LoadingState.Loading) } },
                { currentState.next { copy(loadingState = LoadingState.Data) } }
            )
            .onEach { currentState.next { copy(streams = it) } }
            .retryCall { currentState.next { copy(loadingState = LoadingState.Error()) } }
            .launchIn(viewModelScope)
    }
}
