package com.example.clean.entry.details.presentation

import androidx.lifecycle.viewModelScope
import com.example.clean.entry.core.mvi.BaseViewModel
import com.example.clean.entry.core.navigation.AppDestination
import com.example.clean.entry.core.navigation.AppNavigator
import com.example.clean.entry.details.domain.usecase.GetImageDetailsUseCase
import com.example.clean.entry.details.domain.usecase.GetSimilarImagesUseCase
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val imageId: Long,
    private val getImageDetailsUseCase: GetImageDetailsUseCase,
    private val getSimilarImagesUseCase: GetSimilarImagesUseCase,
    private val navigator: AppNavigator
) : BaseViewModel<ImageDetailsReducer.State, ImageDetailsReducer.Event, ImageDetailsReducer.Effect>(
    reducer = ImageDetailsReducer(),
    initialState = ImageDetailsReducer.State()
) {

    override suspend fun initialDataLoad() {
        super.initialDataLoad()
        handleEvent(ImageDetailsReducer.Event.ScreenOpened(this.imageId))
    }

    override fun handleEvent(event: ImageDetailsReducer.Event) {
        when (event) {
            is ImageDetailsReducer.Event.ScreenOpened -> {
                setState(event)
                event.imageId?.let {
                    fetchData(event.imageId)
                }
            }

            is ImageDetailsReducer.Event.RetryLoadDetails -> {
                val currentId = state.value.imageId ?: this.imageId
                setState(ImageDetailsReducer.Event.ScreenOpened(currentId))
                fetchData(currentId)
            }

            is ImageDetailsReducer.Event.SimilarImageClicked -> {
                navigator.navigate(AppDestination.ImageDetails(event.imageId))
            }

            is ImageDetailsReducer.Event.BackButtonClicked -> {
                navigator.navigateBack()
            }

            else -> setState(event)
        }
    }

    private fun fetchData(id: Long) {
        viewModelScope.launch {
            // Step 1: Fetch main image details
            val mainImageResult = getImageDetailsUseCase(id)

            mainImageResult
                .onSuccess { image ->
                    setState(ImageDetailsReducer.Event.ImageDetailsLoaded(image))

                    val query = image.alt
                    if (!query.isBlank()) {
                        getSimilarImagesUseCase(query = query)
                            .onSuccess { similarImages ->
                                setState(ImageDetailsReducer.Event.SimilarImagesLoaded(similarImages))
                            }
                            .onFailure { throwable ->
                                setState(
                                    ImageDetailsReducer.Event.LoadFailed(
                                        "Error loading similar images: ${throwable.message}"
                                    )
                                )
                            }
                    } else {
                        setState(ImageDetailsReducer.Event.SimilarImagesLoaded(emptyList()))
                    }
                }
                .onFailure { throwable ->
                    setState(
                        ImageDetailsReducer.Event.LoadFailed(
                            throwable.message ?: "Error loading image details"
                        )
                    )
                    setState(ImageDetailsReducer.Event.SimilarImagesLoaded(emptyList()))
                }
        }
    }
}
