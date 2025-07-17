# A Deep Dive Case Study

This document provides a comprehensive overview of the architectural decisions, patterns, and best practices used in the **CleanEntry** project. It is intended to be a learning resource for Android developers interested in modern, scalable application development.

## 1. 🏛️ Core Architecture: Clean Architecture

The project is built upon the principles of **Clean Architecture**. This architectural style separates the software into layers, with a strict dependency rule: outer layers can depend on inner layers, but inner layers know nothing about the outer layers.

- **Why Clean Architecture?**

    - **Independence of Frameworks:** The core business logic is not dependent on the Android framework, making it highly portable.

    - **Testability:** Each layer can be tested in isolation. The domain layer, containing the most critical business rules, can be tested with simple, fast unit tests.

    - **Independence of UI:** The UI can be changed easily without affecting the rest of the system.

    - **Maintainability:** The clear separation of concerns makes the codebase easier to understand, scale, and maintain over time.


### Module Structure

To enforce this separation, the project is organized into distinct Gradle modules:

```
📁 CleanEntry/
├── 📁 app/
│   └── (Main application module)
│
├── 📁 core/
│   └── (Shared code, design system, utilities)
│
└── 📁 feature_auth/
    └── (Self-contained authentication feature)
```

- **:app** - The entry point of the application. It ties all the feature modules together and handles top-level concerns like the main navigation graph.

- **:core** - A foundational library module. It contains code that is shared across multiple features, such as the Design System components, MVI base classes, and common utilities. It is completely independent of any specific feature.

- **:feature_auth** - A self-contained feature module. It holds all the code related to the authentication flow (Login, Registration, Country Code Picker). This module could, in theory, be reused in another application.


## 2. 🔄 Presentation Layer Pattern: MVI (Model-View-Intent)

The presentation layer uses the MVI pattern to create a predictable and robust UI.

- **Why MVI?**

    - **Unidirectional Data Flow:** Data flows in a single, predictable loop (User Intent -> ViewModel -> State -> UI), which makes the app easier to debug and reason about.

    - **Single Source of Truth:** The UI state is represented by a single, immutable `State` object. This eliminates state inconsistency bugs.

    - **Perfect for Compose:** The MVI pattern of observing a single state object aligns perfectly with Jetpack Compose's declarative nature.


### Key MVI Components

- **`BaseViewModel`**: An abstract `ViewModel` provides a consistent structure. It manages the event-handling loop and the channel for one-time side effects (`Effects`), ensuring all ViewModels in the app have a similar, predictable shape.

- **`Reducer`**: Each feature has a `Reducer` object (e.g., `LoginReducer`). This is a **pure function** responsible for taking the current state and an event and producing a new state. Separating this logic makes state mutations centralized, predictable, and trivial to unit test.

- **`Route` & `Screen`**: Each UI screen is split into two composables:

    - The **`Route`** is the stateful component. It connects to the `ViewModel`, collects the state, and handles side effects like navigation.

    - The **`Screen`** is the stateless component. It only knows how to display the UI based on the state it's given and how to emit events back to the `Route`. This makes it highly reusable and easy to test with Jetpack Compose Previews.


## 3. 🎨 Design System

A centralized Design System, located in the `:core` module, ensures visual consistency and speeds up development.

- **Foundations**:

    - `Color.kt`: Defines the application's color palette for both Light and Dark themes.

    - `Type.kt`: Defines the typographic scale (e.g., `titleLarge`, `bodyMedium`).

    - `Shape.kt`: Defines the corner radius values for components.

    - `Spacing.kt`: Defines a consistent scale for padding and margins.

    - `Theme.kt`: The main `AppTheme` composable that brings all the foundations together and provides them to the UI tree.

- **Components**:

    - Reusable, pre-styled composables like `AppButton`, `AppTextField`, and `PasswordTextField` are built using the design system foundations. This ensures all UI elements are consistent and can be updated from a single source of truth.


## 4. 🗺️ Navigation

Navigation is a critical part of the user experience and is designed to be both type-safe and feature-independent.

- **Feature-Owned Navigation**: The `feature_auth` module defines its own navigation graph (`AuthNavHost.kt`) and destinations (`AuthDestination.kt`). This makes the entire feature self-contained and reusable. The main `:app` module simply includes this feature-specific graph into the top-level navigation.

- **Type-Safe Destinations**: We use a `sealed class` to define all navigation routes. This eliminates raw string routes, preventing common runtime crashes from typos and making navigation logic easier to refactor.

- **Returning Results**: To pass data back from one screen to another (e.g., from the `CountryCodePickerScreen`), we use the `NavController`'s `SavedStateHandle`. The destination screen sets a result in the `SavedStateHandle` of the _previous_ screen, which then observes this handle to receive the data. This is the modern, lifecycle-aware, and recommended way to handle screen results in Jetpack Compose.


## 5. 🧪 Testing Strategy

A robust testing strategy ensures the app is reliable and maintainable.

- **Domain Layer**: UseCases are tested with pure JUnit tests to verify all business logic. This layer should have the highest test coverage.

- **Presentation Layer**:

    - **Reducers** are tested to ensure all possible state transitions are correct. Since they are pure functions, these tests are simple and fast.

    - **ViewModels** are tested using libraries like **MockK** (for mocking dependencies like UseCases) and **Turbine** (for testing `StateFlow` emissions). These tests verify that the ViewModel correctly handles events and produces the expected state changes and side effects.

- **UI Layer**:

    - Stateless **`Screen`** composables are tested with Compose UI tests. We can provide mock `State` objects to verify that the UI renders correctly in all possible states (loading, error, success, etc.).

    - End-to-end user flows are tested to ensure the integration between the UI, ViewModel, and other components works as expected.