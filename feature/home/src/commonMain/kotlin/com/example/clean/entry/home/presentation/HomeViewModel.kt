package com.example.clean.entry.home.presentation

import androidx.lifecycle.viewModelScope
import cleanentry.feature.home.generated.resources.Res
import cleanentry.feature.home.generated.resources.error_loading_images
import com.example.clean.entry.core.mvi.BaseViewModel
import com.example.clean.entry.home.domain.usecase.GetImagesUseCase
import com.example.clean.entry.home.presentation.HomeReducer.HomeEvent
import com.example.clean.entry.home.presentation.HomeReducer.HomeState
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getImagesUseCase: GetImagesUseCase
) : BaseViewModel<HomeState, HomeEvent, Nothing>(
    reducer = HomeReducer(),
    initialState = HomeState()
) {
    override suspend fun initialDataLoad() {
        super.initialDataLoad()
        handleEvent(HomeEvent.LoadImages)
    }

    override fun handleEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.LoadImages -> {
                setState(event)
                viewModelScope.launch {
                    val result = getImagesUseCase()
                    result.onSuccess { images ->
                        setState(HomeEvent.LoadImagesSuccess(images))
                    }.onFailure { throwable ->
                        setState(HomeEvent.LoadImagesFailure(Res.string.error_loading_images))
                    }
                }
            }

            else -> setState(event)
        }
    }
}