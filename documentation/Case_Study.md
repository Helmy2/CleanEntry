# A Deep Dive Case Study

This document provides a comprehensive overview of the architectural decisions, patterns, and best
practices used in the CleanEntry project. It is intended to be a learning resource for developers
interested in modern, scalable application development with Kotlin Multiplatform.

## 1. 🏛️ Core Architecture: Clean Architecture

The project is built upon **Clean Architecture** principles, adapted for a Kotlin Multiplatform
environment. This architectural style promotes separation of concerns, making the application
scalable, maintainable, and testable. The core layers are:

- **Domain Layer (Shared Kotlin):** This is the heart of the application, containing the core
  business logic. It is completely independent of any UI or framework specifics.
    - **Use Cases (Interactors):** Encapsulate specific business operations (e.g.,
      `GetImageDetailsUseCase`, `GetImagesUseCase`). They orchestrate data flow from repositories
      and apply business rules.
    - **Domain Models:** Represent core entities of the application (e.g., `Image.kt` in
      `shared/domain/model`). These are pure data classes.
    - **Repository Interfaces:** Define contracts for data operations (e.g., `ImageRepository`),
      abstracting the data sources from the domain layer.

- **Data Layer (Shared Kotlin):** Responsible for providing data to the domain layer. It implements
  the repository interfaces defined in the domain layer.
    - **Repository Implementations:** Concrete implementations of the repository interfaces (e.g.,
      `ImageRepositoryImpl`). They manage data from various sources.
    - **Data Sources:** Interact with external data providers, such as network APIs (e.g., Ktor HTTP
      client for Pexels API) or local databases (e.g., an offline-first country data source
      mentioned).
    - **DTOs (Data Transfer Objects):** Models representing the structure of data from external
      sources (e.g., `PexelsPhoto` from `PexelsPhotosResponse.kt`).
    - **Mappers:** Functions responsible for converting DTOs to Domain Models (e.g.,
      `PexelsPhoto.toDomainModel()` in `shared/data/mapper/mapper.kt`) and vice-versa if needed.

- **Presentation Layer (Platform-Specific UI + Shared KMP ViewModels):** Responsible for displaying
  data to the user and handling user interactions. This layer utilizes the MVI (Model-View-Intent)
  pattern.
    - **Shared ViewModels (KMP):** Contain the presentation logic, manage state, and handle UI
      events (e.g., `ImageDetailsViewModel`, `FeedViewModel`). They interact with Use Cases from the
      domain layer.
    - **Reducers (KMP):** Pure functions that take the current state and an event to produce a new
      state, ensuring predictable state transitions.
    - **UI (Platform-Specific):**
        - **Android:** Jetpack Compose is used to build the UI (`ImageDetailsScreen.kt`,
          `FeedRoute.kt`).
        - **iOS:** SwiftUI is used for the UI (`DetailsView.swift`), connecting to the KMP
          ViewModels via helper classes.
        - **Desktop/Web (WasmJs):** Envisioned to also use KMP ViewModels with appropriate UI
          frameworks.

This layered approach ensures that business logic is decoupled from UI and data source details,
leading to a more robust and flexible system.

## 2. 🔄 Presentation Layer Pattern: MVI (Model-View-Intent)

The presentation layer uses a combination of a shared MVI pattern and a platform-specific UI
strategy to deliver a native experience on each platform.

- **Why MVI?**

    - **Unidirectional Data Flow:** Data flows in a single, predictable loop (User Intent ->
      ViewModel -> State -> UI), which makes the app easier to debug and reason about.

    - **Single Source of Truth:** The UI state is represented by a single, immutable `State` object.
      This eliminates state inconsistency bugs.

    - **Perfect for Compose:** The MVI pattern of observing a single state object aligns perfectly
      with Jetpack Compose's declarative nature.

### Key MVI Components

- **`BaseViewModel`**: An abstract `ViewModel` provides a consistent structure. It manages the
  event-handling loop and the channel for one-time side effects (`Effects`), ensuring all ViewModels
  in the app have a similar, predictable shape.

- **`Reducer`**: Each feature has a `Reducer` object (e.g., `LoginReducer`). This is a **pure
  function** responsible for taking the current state and an event and producing a new state.
  Separating this logic makes state mutations centralized, predictable, and trivial to unit test.

- **`Route` & `Screen`**: Each UI screen is split into two composables:

    - The **`Route`** is the stateful component. It connects to the `ViewModel`, collects the state,
      and handles side effects like navigation.

    - The **`Screen`** is the stateless component. It only knows how to display the UI based on the
      state it's given and how to emit events back to the `Route`. This makes it highly reusable and
      easy to test with Jetpack Compose Previews.

### UI Strategy

The UI strategy aims to maximize code sharing while delivering a native look and feel on each
platform:

- **Shared Presentation Logic:** All presentation logic, state management, and event handling are
  encapsulated within shared KMP ViewModels (extending `BaseViewModel`) and Reducers. These are
  written in common Kotlin and are part of the KMP modules (e.g., `feature:home`, `feature:auth`).

- **Android (Jetpack Compose):**
    - Composable `Screen` functions are responsible for rendering the UI based on the state
      collected from the shared ViewModel (typically using `state.collectAsState()`).
    - `Screen`s are stateless and emit UI events (e.g., button clicks) back to the ViewModel.
    - `Route` composables handle dependency injection (fetching the ViewModel via Koin) and connect
      the ViewModel to the `Screen`.

- **iOS (SwiftUI):**
    - SwiftUI `View`s observe an `ObservableObject` helper class (e.g., `DetailsViewModelHelper`,
      which can extend a `BaseViewModelHelper`).
    - This helper class wraps the KMP ViewModel instance (obtained via Koin, e.g., using
      `KoinIOS.shared`).
    - It subscribes to the KMP ViewModel's state `Flow` and converts it into `@Published` properties
      that the SwiftUI `View` can bind to.
    - UI events from SwiftUI Views are forwarded by the helper to the KMP ViewModel's event handling
      mechanism.

  To facilitate this integration, especially for initializing Koin with platform-specific
  dependencies and accessing KMP ViewModels from Swift, a helper class like
  `DependenciesHelper.kt` (located in
  `composeApp/src/iosMain/kotlin/com/example/clean/entry/shared/util/`) is utilized. This class:
    * Initializes the Koin dependency injection framework for the iOS app during its construction.
      It allows for the injection of platform-specific implementations (such as a
      `PhoneNumberVerifier` provided from Swift) into the shared KMP dependency graph.
    * Acts as a `KoinComponent`, providing easy, idiomatic access from Swift (via an instance of
      `DependenciesHelper`) to shared KMP ViewModels (e.g., `LoginViewModel`, `FeedViewModel`,
      `ImageDetailsViewModel`) and other Koin-managed services like `AppNavigator` or repositories.
    * Offers factory methods for ViewModels that require runtime parameters (e.g.,
      `detailsViewModel(id: SInt64)`), abstracting the Koin parameter-passing mechanism from Swift
      code.
      This makes the setup and usage of shared KMP components within the iOS application more
      streamlined and keeps the Swift-side code cleaner by centralizing KMP dependency access.

  Further supporting this KMP-to-SwiftUI bridge are other key Swift-side components:

    * **`BaseViewModelHelper.swift`**: This generic Swift `ObservableObject` class acts as a
      foundational piece for specific ViewModel helpers on the iOS side (e.g., a
      `DetailsViewModelHelper` would extend this). It's responsible for:
        * Wrapping an instance of a KMP `CoreBaseViewModel` (or its feature-specific subclass).
        * Utilizing Swift's modern concurrency (`AsyncSequence`, `Task`) to subscribe to the `state`
          Flow (and optionally an `effect` Flow if the KMP ViewModel exposes one).
        * Publishing the `currentState` and any `latestEffect` from the KMP ViewModel as
          `@Published` properties, making them directly observable by SwiftUI views.
        * Providing a `handleEvent(event:)` method to channel UI events from SwiftUI views back to
          the shared KMP ViewModel.
          This pattern ensures that the KMP ViewModels remain the single source of truth for UI
          state and logic, while `BaseViewModelHelper` and its subclasses act as efficient,
          lifecycle-aware conduits.

    * **`NavigatorHelper.swift`**: This `ObservableObject` centralizes navigation logic on the iOS
      side, working in tandem with the shared KMP `AppNavigator`. Its key functions include:
        * Obtaining the shared `AppNavigator` instance (typically via `DependenciesHelper`).
        * Subscribing to the `commands` Flow exposed by the KMP `AppNavigator`.
        * Translating the platform-agnostic `CoreAppDestination` and `CoreCommand` objects (from
          KMP) into Swift-specific types that are easier for SwiftUI to work with (e.g., Swift enums
          for destinations and navigation actions).
        * Managing SwiftUI's navigation state, such as `NavigationPath` for `NavigationStack` and
          `selectedTab` for `TabView`, by updating these `@Published` properties in response to
          commands from the KMP `AppNavigator`.
        * It may also handle initial navigation setup, like determining the starting tab based on
          authentication state.
          This helper ensures that navigation is primarily driven by the shared KMP ViewModels,
          while the actual UI transitions are performed using native SwiftUI mechanisms.

- **Desktop & Web (WasmJs):**
    - While specifics might vary, the principle remains the same: the UI for these platforms would
      also connect to the shared KMP ViewModels to consume state and dispatch events.

This approach allows for a single source of truth for presentation logic and state, significantly
reducing duplication and ensuring consistent behavior across platforms.

## 3. 🎨 Design System

A centralized Design System, located in the `:core` module, ensures visual consistency and speeds up
development.

- **Foundations**:

    - `Color.kt`: Defines the application's color palette for both Light and Dark themes.

    - `Type.kt`: Defines the typographic scale (e.g., `titleLarge`, `bodyMedium`).

    - `Shape.kt`: Defines the corner radius values for components.

    - `Spacing.kt`: Defines a consistent scale for padding and margins.

    - `Theme.kt`: The main `AppTheme` composable that brings all the foundations together and
      provides them to the UI tree.

- **Components**:

    - Reusable, pre-styled composables like `AppButton`, `AppTextField`, and `PasswordTextField` are
      built using the design system foundations. This ensures all UI elements are consistent and can
      be updated from a single source of truth.

## 4. 🗺️ Navigation

Navigation in CleanEntry is designed to be orchestrated from shared KMP ViewModels, allowing for
common navigation logic while platform-specific frameworks handle the actual UI transitions.

- **Shared Navigation Contracts:**
    - **`AppNavigator`:** An interface defined in the `core` KMP module. It provides methods for
      navigation actions (e.g., `navigate(destination: AppDestination)`, `navigateBack()`). This
      service is injected into ViewModels that require navigation capabilities.
    - **`AppDestination`:** A sealed class (or enum) located in the `core` module. It defines all
      possible navigation routes within the application, including any parameters they might
      require (e.g., `ImageDetails(imageId: Long)`).

- **ViewModel-Driven Navigation:**
    - ViewModels request navigation by calling methods on the injected `AppNavigator` instance. This
      typically happens as a result of a UI event or a business logic outcome (e.g., after
      successful login, navigate to home).
    - Navigation commands can also be exposed as one-time `Effect`s from the `BaseViewModel`.

- **Platform-Specific Implementation:**
    - **Android:** The `AppNavigator` implementation, provided via Koin, likely interacts with a
      Jetpack Navigation `NavController` or a custom Composable navigation solution (as seen in
      `AppNavHost.kt` in the `composeApp` module). It translates `AppDestination` objects into
      actual navigation actions.
    - **iOS:** The `AppNavigator` implementation for iOS would trigger mechanisms that the Swift UI
      layer can observe. This is often handled by a coordinator pattern (e.g.,
      `AppCoordinatorView.swift`). The coordinator listens to navigation commands (which could be
      `Flow`s or `Channel`s exposed by the KMP `AppNavigator` or via `Effect`s) and uses SwiftUI's
      navigation tools (`NavigationStack`, sheets, full-screen covers, etc.) to perform the UI
      transition.

This setup ensures that navigation logic remains primarily in the shared KMP code, making it
consistent and easier to manage across different platforms.

## 5. 🧪 Quality Assurance

### Testing Strategy (not implemented yet)

The project's Clean Architecture and MVI pattern inherently promote testability across different
layers:

- **Domain Layer:**
    - **Use Cases:** As they contain business logic and have clear dependencies (typically
      repository interfaces), Use Cases are highly suitable for unit testing. Dependencies can be
      easily mocked.
    - **Domain Models:** Being simple data classes, they usually don't require extensive testing
      beyond ensuring data integrity if they have complex construction logic.

- **Data Layer:**
    - **Repositories:** Repository implementations can be unit tested by mocking their data
      sources (e.g., mock Ktor `HttpClient` responses or mock database DAOs).
    - **Mappers:** Mapper functions (e.g., `PexelsPhoto.toDomainModel()`) are pure functions and can
      be thoroughly unit tested to ensure correct DTO-to-DomainModel conversion.

- **Presentation Layer (Shared KMP Logic):**
    - **Reducers:** As pure functions `(CurrentState, Event) -> NewState`, Reducers are
      straightforward to unit test. Given an initial state and an event, the resulting state can be
      asserted.
    - **ViewModels:** Shared KMP ViewModels can be unit tested. Dependencies like Use Cases and
      `AppNavigator` can be mocked. Tests can verify that:
        - Correct state transformations occur based on dispatched events.
        - Appropriate Use Cases are called.
        - Expected navigation `Effect`s or commands are triggered.

- **Platform-Specific UI Testing:**
    - **Android (Jetpack Compose):** Jetpack Compose provides testing APIs (`ComposeTestRule`) for
      UI testing, allowing interaction with Composable elements and verification of their state and
      appearance.
    - **iOS (SwiftUI):** XCTest framework can be used for UI testing SwiftUI views, ensuring UI
      elements are displayed correctly and respond to user interactions.

- **Integration Testing:**
    - Tests can be designed to verify the interaction between different layers, such as the flow
      from ViewModel event handling through Use Cases and Repositories.

The separation of concerns makes it easier to write focused and effective tests at each level of the
application.

### Continuous Integration (CI)

- The CI/CD platform used (e.g., GitHub Actions, Jenkins, GitLab CI).
- The types of jobs configured (e.g., build checks for all platforms, linting, running unit tests,
  running UI tests).
- Deployment strategies, if automated.
