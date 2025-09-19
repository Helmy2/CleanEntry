package com.example.clean.entry.home.presentation

import com.example.clean.entry.core.mvi.Reducer
import com.example.clean.entry.home.domain.model.Image
import org.jetbrains.compose.resources.StringResource

class HomeReducer : Reducer<HomeReducer.HomeState, HomeReducer.HomeEvent, Nothing> {
    override fun reduce(
        previousState: HomeState,
        event: HomeEvent
    ): Pair<HomeState, Nothing?> {
        return when (event) {
            HomeEvent.LoadImages -> previousState.copy(isLoading = true, error = null) to null
            is HomeEvent.LoadImagesSuccess -> previousState.copy(
                images = event.images,
                isLoading = false,
                error = null
            ) to null

            is HomeEvent.LoadImagesFailure -> previousState.copy(
                isLoading = false,
                error = event.error
            ) to null
        }
    }

    sealed class HomeEvent : Reducer.ViewEvent {
        data object LoadImages : HomeEvent()
        data class LoadImagesSuccess(val images: List<Image>) : HomeEvent()
        data class LoadImagesFailure(val error: StringResource) : HomeEvent()
    }

    data class HomeState(
        val isLoading: Boolean = false,
        val images: List<Image> = emptyList(),
        val error: StringResource? = null
    ) : Reducer.ViewState
}