package com.example.clean.entry.feed.presentation

import androidx.lifecycle.viewModelScope
import cleanentry.feature.home.generated.resources.Res
import cleanentry.feature.home.generated.resources.error_loading_images
import com.example.clean.entry.core.mvi.BaseViewModel
import com.example.clean.entry.feed.domain.usecase.GetImagesUseCase
import com.example.clean.entry.feed.presentation.FeedReducer.Effect
import com.example.clean.entry.feed.presentation.FeedReducer.Event
import com.example.clean.entry.feed.presentation.FeedReducer.State
import kotlinx.coroutines.launch

class FeedViewModel(
    private val getImagesUseCase: GetImagesUseCase
) : BaseViewModel<State, Event, Effect>(
    reducer = FeedReducer(),
    initialState = State()
) {
    override suspend fun initialDataLoad() {
        super.initialDataLoad()
        handleEvent(Event.LoadImages)
    }

    override fun handleEvent(event: Event) {
        when (event) {
            Event.LoadImages -> {
                setState(event)
                viewModelScope.launch {
                    val result = getImagesUseCase()
                    result.onSuccess { images ->
                        setState(Event.LoadImagesSuccess(images))
                    }.onFailure { throwable ->
                        setState(Event.LoadImagesFailure(Res.string.error_loading_images))
                    }
                }
            }

            else -> setState(event)
        }
    }
}