# Using a Kotlin Multiplatform ViewModel in SwiftUI: Login Feature Example

This document explains how a Kotlin Multiplatform (KMP) `ViewModel` can be integrated and used
within a SwiftUI view, using the project's Login feature as a practical example. This approach
allows for sharing presentation logic and state management across platforms while leveraging native
UI on iOS.

## Core KMP Presentation Components

Our KMP architecture uses a shared `BaseViewModel` and feature-specific `Reducer` objects:

* **`BaseViewModel<State, Event, Effect>`:** A KMP class that manages the state, processes UI
  events, and can emit one-time side effects.
* **`Reducer<State, Event, Effect>`:** An object responsible for pure state transformations. It
  takes the current `State` and an `Event` and produces a new `State` and optionally an `Effect`.

## The Login Feature Example - KMP Side

The Login feature's presentation logic resides in the `feature/auth` KMP module.

### 1. `LoginReducer.kt`

This object defines the structure of the state, UI events, and side effects for the login screen.

* **`LoginReducer.State`**:
    * Holds data like `email`, `phone`, `password`, `otp`, `selectedCountry`, `authMethod`,
      `isLoading`, `error` messages for validation, `verificationId`, and UI derived state like
      `isLoginButtonEnabled`.

  ```kotlin
  // Snippet from LoginReducer.kt
  data class State(
      val email: String = "",
      val emailError: String? = null,
      val phone: String = "",
      val phoneError: String? = null,
      val password: String = "",
      // ... other fields
      val isLoading: Boolean = false,
      val error: String? = null,
      val verificationId: String? = null,
      val otp: String = ""
      // ...
  ) : Reducer.ViewState {
      val isLoginButtonEnabled
          get() =  /* ... logic ... */
  }
  ```

* **`LoginReducer.Event`**:
    * Defines all possible user interactions and internal events, such as `EmailChanged`,
      `PhoneChanged`, `PasswordChanged`, `OtpChanged`, `Submit`, `CountrySelected`,
      `AuthMethodChanged`, `LoginSuccess`, `LoginFailed`, `VerificationCodeSent`,
      `BackButtonClicked`, `CreateAccountClicked`, `CountryButtonClick`.

  ```kotlin
  // Snippet from LoginReducer.kt
  sealed interface Event : Reducer.ViewEvent {
      data class EmailChanged(val value: String) : Event
      data class PasswordChanged(val value: String) : Event
      data object Submit : Event
      // ... other events
  }
  ```

* **`LoginReducer.Effect`**:
    * For the Login feature, this sealed interface is currently empty. One-time actions like
      navigation are handled directly within the `LoginViewModel` by calling the `AppNavigator`.

  ```kotlin
  // Snippet from LoginReducer.kt
  sealed interface Effect : Reducer.ViewEffect
  // No specific effects defined for Login as of this writing.
  ```

### 2. `LoginViewModel.kt`

This KMP ViewModel orchestrates the login logic:

* It extends `BaseViewModel`, providing it with the `LoginReducer` and an initial
  `LoginReducer.State()`.
* It handles events (e.g., text input changes, button clicks) by validating input (using injected
  UseCases like `ValidateEmailUseCase`) and dispatching corresponding events to the `LoginReducer`
  to update the state.
* For actions like `Submit`, it calls methods on the `AuthRepository` (e.g.,
  `loginWithEmailAndPassword`, `sendVerificationCode`).
* Navigation is performed by calling methods on the injected `AppNavigator` (e.g.,
  `navigator.navigate(AppDestination.Registration)`).

```kotlin
// Snippet from LoginViewModel.kt
class LoginViewModel(
    // ... dependencies like UseCases, AuthRepository, AppNavigator
) : BaseViewModel<LoginReducer.State, LoginReducer.Event, LoginReducer.Effect>(
    reducer = LoginReducer,
    initialState = LoginReducer.State()
) {
    override fun handleEvent(event: LoginReducer.Event) {
        when (event) {
            is LoginReducer.Event.EmailChanged -> {
                val result = validateEmailUseCase(event.value)
                // Update state via reducer
                setState(LoginReducer.Event.EmailUpdated(event.value, result))
            }
            is LoginReducer.Event.Submit -> {
                submitLogin() // Calls authRepository
                setState(event) // Updates isLoading, etc.
            }
            is LoginReducer.Event.CreateAccountClicked -> {
                navigator.navigate(AppDestination.Registration) // Direct navigation
            }
            // ... other event handling
        }
    }
    // ...
}
```

## The iOS Bridge: Connecting KMP to SwiftUI

To use the KMP `LoginViewModel` in SwiftUI, several bridge components are involved:

### 1. `DependenciesHelper.kt` (KMP `iosMain`)

This Kotlin class, located in `composeApp/src/iosMain/...`, is crucial for initializing Koin on iOS
and providing access to KMP ViewModels.

* It initializes Koin with any necessary platform-specific modules (e.g., for
  `PhoneNumberVerifier`).
* It exposes KMP ViewModel instances (like `loginViewModel`) as properties, making them easily
  accessible from Swift.

```kotlin
// Snippet from DependenciesHelper.kt
class DependenciesHelper : KoinComponent {
    constructor(phoneNumberValidator: PhoneNumberVerifier) {
        initKoin(
            platformModule = module { /* ... */ }
        )
    }

    // ...
    val loginViewModel: LoginViewModel by inject()
    // ...
}
```

This is typically instantiated once in the Swift `App`'s `init` or as a global singleton (
`iOSApp.dependenciesHelper`).

### 2. `BaseViewModelHelper.swift` (Swift)

This generic Swift `ObservableObject` class (in `iosApp/iosApp/Core/`) serves as the foundation for
all specific ViewModel helpers.

* It wraps a KMP `CoreBaseViewModel` instance.
* It uses Swift's modern concurrency (`AsyncSequence`, `Task`) to subscribe to the KMP ViewModel's
  `state` Flow (and `effect` Flow, if applicable).
* It publishes the `currentState` (and `latestEffect`) making them available to SwiftUI views.
* It provides a `handleEvent(event:)` method to forward UI events from Swift back to the KMP
  ViewModel.

### 3. `LoginViewModelHelper.swift` (Swift)

This class (in `iosApp/iosApp/Screens/Login/`) is specific to the Login feature and bridges the KMP
`LoginViewModel` to SwiftUI.

* It extends `BaseViewModelHelper`, specifying the KMP `LoginReducer.State`, `LoginReducer.Event`,
  and `LoginReducer.Effect` types (note: these KMP types are often prefixed with the shared module
  name like `shared.LoginReducerState` or aliased in Swift, here it appears as
  `AuthLoginReducerState`, etc., due to how Kotlin framework is built and imported).
* In its initializer, it retrieves the `LoginViewModel` instance from
  `iOSApp.dependenciesHelper.loginViewModel`.
* It passes this KMP ViewModel instance and its initial state to the `super.init()` of
  `BaseViewModelHelper`.

```swift
// Snippet from LoginViewModelHelper.swift

import shared // Import the KMP shared module

class LoginViewModelHelper: BaseViewModelHelper<AuthLoginReducerState, AuthLoginReducerEvent, AuthLoginReducerEffect> {
    init() {
        let vm = iOSApp.dependenciesHelper.loginViewModel
        super.init(viewModel: vm, initialState: vm.state.value as! AuthLoginReducerState)
        // Note: The 'as! AuthLoginReducerState' cast might be necessary depending on
        // how the KMP state type is exposed to Swift.
        // It's often shared.LoginReducerState or a typealias.
    }
}
```

## The SwiftUI View: `LoginView.swift`

The `LoginView.swift` (in `iosApp/iosApp/Screens/Login/`) is responsible for rendering the UI based
on the state from `LoginViewModelHelper` and forwarding user interactions.

* **Instantiating the Helper:** It uses `@StateObject` to create and keep alive an instance of
  `LoginViewModelHelper`.

  ```swift
  // Snippet from LoginView.swift
  struct LoginView: View {
      @StateObject var viewModel = LoginViewModelHelper()
      // ...
  }
  ```

* **Reading State:** The view reads `viewModel.currentState` (which is a `@Published` property from
  `BaseViewModelHelper`) to configure its UI elements.

  ```swift
  // Snippet from LoginView.swift
  VStack(alignment: .leading) {
      TextField("Email", text: $viewModel.email) // Uses extension for direct binding
          .keyboardType(.emailAddress)
      if let error = viewModel.currentState.emailError { // Accessing state for error
          Text(error as! String) // Cast may be needed for Kotlin String
              .font(.caption)
              .foregroundColor(.red)
      }
  }
  // ...
  Button(action: {
      viewModel.handleEvent(event: AuthLoginReducerEventSubmit())
  }) {
      Text("Login")
  }
  .disabled(!viewModel.currentState.isLoginButtonEnabled) // Accessing derived state
  ```

* **Two-Way Binding and Sending Events:** For UI elements like `TextField` and `Picker`,
  `LoginView.swift` utilizes convenient extensions on `LoginViewModelHelper`. These extensions
  provide `Binding`-like behavior, reading the current state for the `get` and calling
  `viewModel.handleEvent(event: ...)` in the `set` when the UI control's value changes.

  ```swift
  // Snippet of extension from LoginView.swift (or a similar location)
  extension LoginViewModelHelper {
      @MainActor var email: String {
          get {
              (currentState.email as? String) ?? "" // Read from KMP state
          }
          set {
              // Send KMP event when Swift UI changes the value
              handleEvent(event: AuthLoginReducerEventEmailChanged(value: newValue))
          }
      }
      // Similar extensions for password, phone, otp, authMethod
  }
  ```
  This pattern keeps the SwiftUI view cleaner by encapsulating the event dispatching logic within
  these computed properties.

* **Handling Actions:** For button taps or other direct actions, the view calls
  `viewModel.handleEvent(event:)` with the appropriate KMP `Event`.

  ```swift
  // Snippet from LoginView.swift
  Button(action: {
      viewModel.handleEvent(event: AuthLoginReducerEventCreateAccountClicked())
  }) {
      Text("Create Account")
  }
  ```

## Data Flow Summary

1. **SwiftUI View (`LoginView.swift`):** User interacts (e.g., types in a TextField, taps a button).
2. **Swift Helper (`LoginViewModelHelper.swift`):**
    * If using binding extensions, the `set` block of the computed property is triggered, calling
      `handleEvent()` with a KMP `Event` (e.g., `AuthLoginReducerEventEmailChanged`).
    * For direct actions, the view calls `handleEvent()` directly.
3. **KMP ViewModel (`LoginViewModel.kt`):**
    * The `handleEvent()` method in `LoginViewModel` receives the event.
    * It performs business logic (e.g., validation, calls to repository).
    * It may call `setState()` with a new event for the `LoginReducer` to update the state.
    * It may trigger navigation via `AppNavigator`.
4. **KMP Reducer (`LoginReducer.kt`):**
    * The `reduce()` function takes the current state and the event from the ViewModel, and returns
      a new state.
5. **KMP ViewModel (`LoginViewModel.kt`):**
    * The `state` Flow emits the new state.
6. **Swift Helper (`BaseViewModelHelper.swift` / `LoginViewModelHelper.swift`):**
    * The `Task` observing the KMP `state` Flow receives the new state.
    * It updates its `@Published var currentState`.
7. **SwiftUI View (`LoginView.swift`):**
    * Being an observer of `LoginViewModelHelper` (via `@StateObject`), SwiftUI automatically
      re-renders the relevant parts of the view based on the changes in `currentState`.

This unidirectional data flow ensures that the UI is always a reflection of the KMP ViewModel's
state, and state modifications are handled in a structured and predictable manner within the shared
KMP logic.
