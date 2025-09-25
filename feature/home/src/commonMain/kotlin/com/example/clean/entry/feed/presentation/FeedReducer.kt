package com.example.clean.entry.feed.presentation

import com.example.clean.entry.core.mvi.Reducer
import com.example.clean.entry.feed.domain.model.Image
import org.jetbrains.compose.resources.StringResource

class FeedReducer : Reducer<FeedReducer.State, FeedReducer.Event, FeedReducer.Effect> {
    override fun reduce(
        previousState: State,
        event: Event
    ): Pair<State, Nothing?> {
        return when (event) {
            Event.LoadImages -> previousState.copy(isLoading = true, error = null) to null

            is Event.LoadImagesSuccess -> previousState.copy(
                images = event.images,
                isLoading = false,
                error = null
            ) to null

            is Event.LoadImagesFailure -> previousState.copy(
                isLoading = false,
                error = event.error
            ) to null
        }
    }

    sealed class Event : Reducer.ViewEvent {
        data object LoadImages : Event()
        data class LoadImagesSuccess(val images: List<Image>) : Event()
        data class LoadImagesFailure(val error: StringResource) : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val images: List<Image> = emptyList(),
        val error: StringResource? = null
    ) : Reducer.ViewState

    sealed class Effect : Reducer.ViewEffect
}