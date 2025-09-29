package com.example.clean.entry.details.presentation

import com.example.clean.entry.core.mvi.Reducer
import com.example.clean.entry.shared.domain.model.Image

class ImageDetailsReducer :
    Reducer<ImageDetailsReducer.State, ImageDetailsReducer.Event, ImageDetailsReducer.Effect> {

    data class State(
        val imageId: Long? = null,
        val currentImage: Image? = null,
        val similarImages: List<Image> = emptyList(),
        val isLoading: Boolean = false,
        val isLoadingSimilar: Boolean = false,
        val error: String? = null,
        val shouldDownloadImage: Boolean = false
    ) : Reducer.ViewState

    sealed interface Event : Reducer.ViewEvent {
        data class ScreenOpened(val imageId: Long?) : Event
        data object RetryLoadDetails : Event
        data object BackButtonClicked : Event
        data class SimilarImageClicked(val imageId: Long) : Event
        data object DownloadImageClicked : Event
        data object DismissDownload : Event
        data class ImageDetailsLoaded(val image: Image) : Event
        data class SimilarImagesLoaded(val images: List<Image>) : Event
        data class LoadFailed(val errorMessage: String) : Event
    }

    sealed interface Effect : Reducer.ViewEffect


    override fun reduce(
        previousState: State,
        event: Event
    ): Pair<State, Effect?> {
        return when (event) {
            is Event.ScreenOpened -> {
                previousState.copy(
                    imageId = event.imageId,
                    isLoading = true,
                    error = null,
                    similarImages = emptyList(),
                    currentImage = null
                ) to null
            }

            is Event.ImageDetailsLoaded -> {
                previousState.copy(currentImage = event.image, isLoading = false) to null
            }

            is Event.SimilarImagesLoaded -> {
                previousState.copy(similarImages = event.images, isLoadingSimilar = false) to null
            }

            is Event.LoadFailed -> {
                previousState.copy(
                    isLoading = false,
                    isLoadingSimilar = false,
                    error = event.errorMessage
                ) to null
            }

            is Event.RetryLoadDetails -> {
                previousState.copy(
                    isLoading = true,
                    error = null,
                    isLoadingSimilar = true
                ) to null
            }

            Event.DownloadImageClicked -> {
                previousState.currentImage?.large?.takeIf { it.isNotEmpty() }?.let { imageUrl ->
                    previousState.copy(shouldDownloadImage = true) to null
                } ?: (previousState to null)
            }

            Event.DismissDownload -> {
                previousState.copy(shouldDownloadImage = false) to null
            }

            else -> {
                previousState to null
            }
        }
    }
}
