# MVI Pattern: BaseViewModel and Reducer in CleanEntry

This document details the Model-View-Intent (MVI) architectural pattern implementation within the
CleanEntry project, focusing on the roles and interaction of `BaseViewModel` and the `Reducer`
interface.

## Introduction to MVI in CleanEntry

CleanEntry employs an MVI pattern for its presentation layer to ensure a unidirectional data flow,
immutable state, and clear separation of concerns. This makes the UI logic more predictable,
testable, and maintainable.

The core components of our MVI setup are:

* **State (`S`):** An immutable data class representing the complete UI state for a screen or
  feature.
* **Event (`E`):** Represents user interactions (e.g., button clicks, text input) or other
  occurrences that the ViewModel needs to process.
* **Effect (`F`):** Represents one-time side effects that the UI should perform (e.g., showing a
  toast, navigating to another screen).
* **ViewModel:** Orchestrates the logic, processes `Event`s, interacts with UseCases/Repositories,
  and manages the `State` and `Effect` streams.
* **Reducer:** A pure function responsible for taking the current `State` and an `Event` to produce
  a new `State` and, optionally, an `Effect`.

## The `Reducer` Interface

The `Reducer` is a fundamental concept in our MVI implementation. It's defined as an interface in
`core/src/commonMain/kotlin/com/example/clean/entry/core/mvi/Reducer.kt`.

* **Purpose:** To encapsulate all state mutation logic. It's a pure function, meaning for a given
  input (previous state and event), it always produces the same output (new state and optional
  effect) without causing any side effects itself.
* **Key Components:**
    * `ViewState`: A marker interface for all state objects.
    * `ViewEvent`: A marker interface for all event objects.
    * `ViewEffect`: A marker interface for all effect objects.
    * `reduce(previousState: S, event: E): Pair<S, F?>`: The core function that calculates the new
      state and an optional effect.

```kotlin
// Snippet from core/src/commonMain/kotlin/com/example/clean/entry/core/mvi/Reducer.kt

interface Reducer<S : Reducer.ViewState, E : Reducer.ViewEvent, F : Reducer.ViewEffect> {
    interface ViewState
    interface ViewEvent
    interface ViewEffect

    fun reduce(previousState: S, event: E): Pair<S, F?>
}
```

## The `BaseViewModel`

The `BaseViewModel` (`core/src/commonMain/kotlin/com/example/clean/entry/core/mvi/BaseViewModel.kt`)
is an abstract class that all feature-specific ViewModels extend. It provides the common MVI
machinery.

* **Role:**
    * Holds the current `State` in a `StateFlow`.
    * Provides a `Channel` for emitting one-time `Effect`s.
    * Exposes an abstract `handleEvent(event: E)` method for concrete ViewModels to implement their
      specific event handling logic.
    * Uses an injected `Reducer` instance to calculate new states.
    * Includes a `TimeCapsule` for state debugging (time travel).
* **Key Features:**
    * `state: StateFlow<S>`: Exposes the current UI state to observers (e.g., Composable screens or
      SwiftUI views).
    * `effect: Flow<F>`: Exposes one-time side effects.
    * `handleEvent(event: E)`: Abstract method to be implemented by subclasses to process incoming
      events. This is where business logic, UseCase calls, and repository interactions typically
      occur.
    * `setState(event: E)`: A protected method that takes an event, passes the current state and
      this event to the `reducer`'s `reduce` function, and then updates the `_state` Flow with the
      new state and sends any new `Effect`.
    * `initialDataLoad()`: An open suspend function that can be overridden for initial data loading
      when the ViewModel is created.
    * `timeCapsule`: An interesting feature for state debugging, potentially allowing for
      time-travel debugging of states.

```kotlin
// Snippet from core/src/commonMain/kotlin/com/example/clean/entry/core/mvi/BaseViewModel.kt
abstract class BaseViewModel<S : Reducer.ViewState, E : Reducer.ViewEvent, F : Reducer.ViewEffect>(
    private val reducer: Reducer<S, E, F>,
    initialState: S
) : ViewModel() {

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state
    // ... (onStart logic with initialDataLoad and timeCapsule)

    private val _effect: Channel<F> = Channel()
    val effect = _effect.receiveAsFlow()

    val timeCapsule: TimeCapsule<S> = TimeTravelCapsule { /* ... */ }

    open suspend fun initialDataLoad() {}

    abstract fun handleEvent(event: E)

    protected fun setState(event: E) {
        val (newState, newEffect) = reducer.reduce(_state.value, event)
        if (_state.tryEmit(newState)) {
            timeCapsule.addState(newState)
        }
        newEffect?.let { _effect.trySend(it) }
    }
}
```

## Why Separate the `Reducer` from the `ViewModel`?

Separating the `Reducer` logic from the `ViewModel` offers several significant advantages:

1. **Enhanced Testability:**
    * Reducers are pure functions. This makes them incredibly easy to unit test. You can provide a
      specific `previousState` and an `event` and assert that the `newState` and `newEffect` are
      exactly as expected, without mocking any dependencies or dealing with asynchronous behavior.

2. **Predictability and Simplicity of State Mutations:**
    * All state changes are centralized within the `reduce` function of a `Reducer`. This makes it
      easier to understand how and why the state changes. Debugging state-related issues becomes
      simpler as there's only one place where state transitions are defined.

3. **Single Responsibility Principle (SRP):**
    * **Reducer:** Its sole responsibility is to calculate a new state based on the current state
      and an event. It doesn't know about data sources, navigation, or any other side effects.
    * **ViewModel:** Its responsibilities include handling UI events, orchestrating calls to
      UseCases or Repositories (which may involve side effects like network requests or database
      operations), managing coroutine scopes, and triggering navigation. It delegates the actual
      state calculation to the `Reducer`.

4. **Reusability (Potentially):**
    * While not always a primary goal, pure reducer logic could potentially be reused in different
      contexts if the state and event structures align.

## How `BaseViewModel` and a Specific `Reducer` Interact

Let's use the `Login` feature as an example to illustrate the interaction.

### 1. `LoginReducer.kt`

This is an `object` that implements the `Reducer` interface for the login screen.

* It defines its own `State`, `Event`, and `Effect` sealed interfaces (or data classes).
* The `reduce` function contains a `when` expression to handle different `LoginReducer.Event` types
  and return the corresponding new `LoginReducer.State` and an optional `LoginReducer.Effect`.

```kotlin
// Snippet from feature/auth/src/commonMain/kotlin/com/example/clean/entry/auth/presentation/login/LoginReducer.kt
object LoginReducer : Reducer<LoginReducer.State, LoginReducer.Event, LoginReducer.Effect> {

    data class State(
        val email: String = "",
        val emailError: String? = null,
        // ... other state fields
        val isLoading: Boolean = false,
        val error: String? = null
    ) : Reducer.ViewState { /* ... */ }

    sealed interface Event : Reducer.ViewEvent {
        data class EmailChanged(val value: String) : Event
        data class EmailUpdated(val value: String, val result: ValidationResult) : Event
        data object Submit : Event
        // ... other events
    }

    sealed interface Effect : Reducer.ViewEffect // Empty for Login feature

    override fun reduce(
        previousState: State,
        event: Event
    ): Pair<State, Effect?> { // Effect is Nothing? due to empty Effect interface
        return when (event) {
            is Event.EmailUpdated -> previousState.copy(
                email = event.value,
                emailError = event.result.errorMessage
            ) to null // No effect

            is Event.Submit -> previousState.copy(
                isLoading = true, error = null
            ) to null

            is Event.LoginSuccess -> State() to null // Reset state on success

            is Event.LoginFailed -> previousState.copy(
                isLoading = false, error = event.error, otp = ""
            ) to null
            // ... other event handling to produce new state
            else -> previousState to null
        }
    }
}
```

### 2. `LoginViewModel.kt`

This ViewModel extends `BaseViewModel`, providing the `LoginReducer` and an initial state.

* It's initialized with `LoginReducer` and `LoginReducer.State()`.
* The `handleEvent(event: LoginReducer.Event)` method implements the logic for each specific login
  event.
    * For events that directly cause a state change after some validation (e.g., `EmailChanged`), it
      might call a use case and then call `setState(LoginReducer.Event.EmailUpdated(...))` with the
      validation result.
    * For events that trigger asynchronous operations (e.g., `Submit`), it launches a coroutine,
      calls the repository, and then based on the success/failure, calls `handleEvent` again with an
      internal event (like `LoginReducer.Event.LoginSuccess` or `LoginReducer.Event.LoginFailed`)
      which in turn calls `setState` to update the UI.

```kotlin
// Snippet from feature/auth/src/commonMain/kotlin/com/example/clean/entry/auth/presentation/login/LoginViewModel.kt
class LoginViewModel(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val authRepository: AuthRepository,
    private val navigator: AppNavigator,
) : BaseViewModel<LoginReducer.State, LoginReducer.Event, LoginReducer.Effect>(
    reducer = LoginReducer, // Injects the LoginReducer
    initialState = LoginReducer.State()
) {
    override fun handleEvent(event: LoginReducer.Event) {
        when (event) {
            is LoginReducer.Event.EmailChanged -> {
                val result = validateEmailUseCase(event.value)
                // This will use LoginReducer to get the new state
                setState(LoginReducer.Event.EmailUpdated(event.value, result))
            }

            is LoginReducer.Event.PasswordChanged -> {
                val result = validatePasswordUseCase(event.value)
                setState(LoginReducer.Event.PasswordUpdated(event.value, result))
            }

            is LoginReducer.Event.Submit -> {
                // Set loading state immediately via Reducer
                setState(event) // Passes the Submit event to LoginReducer
                // Then perform async work
                submitLogin() // This internal function will call authRepository and then
                // handleEvent(LoginReducer.Event.LoginSuccess/Failed)
            }

            is LoginReducer.Event.CreateAccountClicked -> {
                navigator.navigate(AppDestination.Registration)
                // This event doesn't directly change LoginViewModel's state via its reducer,
                // but causes a navigation side-effect.
            }

            // For events that are purely state updates managed by the reducer:
            is LoginReducer.Event.LoginSuccess,
            is LoginReducer.Event.LoginFailed,
            is LoginReducer.Event.VerificationCodeSent,
            is LoginReducer.Event.AuthMethodChanged,
            is LoginReducer.Event.OtpChanged -> {
                setState(event)
            }

            // ... other specific event handling
            else -> setState(event) // Default to pass event to reducer if not specially handled
        }
    }

    private fun submitLogin() {
        viewModelScope.launch {
            val state = state.value // Current state
            // ... (logic to call authRepository based on authMethod) ...
            // On success:
            // handleEvent(LoginReducer.Event.LoginSuccess) -> which calls setState(LoginSuccess)
            // On failure:
            // handleEvent(LoginReducer.Event.LoginFailed(errorMessage)) -> calls setState(LoginFailed)
        }
    }
}
```

**Flow of an Event:**

1. UI triggers an `Event` (e.g., user types in email field).
2. The `LoginView` (or its helper) calls
   `loginViewModel.handleEvent(LoginReducer.Event.EmailChanged("new email"))`.
3. `LoginViewModel.handleEvent()`:
    * Receives `EmailChanged`.
    * Calls `validateEmailUseCase`.
    * Calls `setState(LoginReducer.Event.EmailUpdated("new email", validationResult))`.
4. `BaseViewModel.setState()`:
    * Calls `LoginReducer.reduce(currentState, EmailUpdatedEvent)`.
5. `LoginReducer.reduce()`:
    * Returns `Pair(newStateWithEmailAndError, nullEffect)`.
6. `BaseViewModel.setState()`:
    * Emits `newStateWithEmailAndError` to the `_state` Flow.
    * The UI observes this `state` Flow and re-renders.

## Benefits of This MVI Approach in CleanEntry

* **Clear Separation of Concerns:** Logic for event handling, side effects (ViewModel) vs. state
  computation (Reducer) is distinct.
* **Improved Testability:** Reducers are easily unit-tested. ViewModels can be tested by mocking
  dependencies and verifying state transitions and effect emissions.
* **Consistent State Management:** All features follow the same pattern for state updates, making
  the codebase easier to understand and navigate.
* **Debugging:** The `TimeCapsule` feature provides a mechanism for easier state debugging.
  Unidirectional data flow and explicit state changes simplify tracing issues.

This pattern provides a robust and scalable foundation for the presentation layer in the CleanEntry
application.
