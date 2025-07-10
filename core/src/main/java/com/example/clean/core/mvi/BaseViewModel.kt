package com.example.clean.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * An abstract ViewModel that implements the MVI (Model-View-Intent) pattern.
 * It provides a structured way to manage state, events, and side effects.
 *
 * @param S The type of the ViewState, representing the UI state.
 * @param E The type of the ViewEvent, representing user actions or other events.
 * @param F The type of the ViewEffect, representing one-time side effects (e.g., navigation, toasts).
 * @property reducer The [Reducer] instance responsible for calculating new states based on current state and events.
 * @property initialState The initial [S] (ViewState) of the ViewModel.
 */
abstract class BaseViewModel<S : Reducer.ViewState, E : Reducer.ViewEvent, F : Reducer.ViewEffect>(
    private val reducer: Reducer<S, E, F>,
    initialState: S
) : ViewModel() {

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state
        .onStart {
            viewModelScope.launch {
                initialDataLoad()
                timeCapsule.addState(initialState)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = initialState
        )

    private val _event: MutableSharedFlow<E> = MutableSharedFlow()
    val event: SharedFlow<E> = _event.asSharedFlow()

    private val _effect: Channel<F> = Channel()
    val effect = _effect.receiveAsFlow()

    val timeCapsule: TimeCapsule<S> = TimeTravelCapsule { storedState ->
        _state.tryEmit(storedState)
    }


    open suspend fun initialDataLoad() {}

    /**
     * The abstract method where concrete ViewModels handle business logic for each event.
     * This is where you would launch coroutines for async work.
     *
     * @param event The event to handle.
     */
    abstract fun handleEvent(event: E)

    /**
     * Sends the current state and an event to the reducer to calculate the new state and effect.
     * The new state is then emitted, and the effect is sent to the effect channel.
     * This should be called from within [handleEvent] for synchronous state updates.
     */
    protected fun setState(event: E) {
        val (newState, newEffect) = reducer.reduce(_state.value, event)
        if (_state.tryEmit(newState)) {
            timeCapsule.addState(newState)
        }
        newEffect?.let { _effect.trySend(it) }
    }
}