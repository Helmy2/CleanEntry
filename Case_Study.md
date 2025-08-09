# A Deep Dive Case Study

This document provides a comprehensive overview of the architectural decisions, patterns, and best
practices used in the CleanEntry project. It is intended to be a learning resource for developers
interested in modern, scalable application development with Kotlin Multiplatform.

## 1. 🏛️ Core Architecture: Clean Architecture

The project is built upon the principles of**Clean Architecture**. This style is applied within the
`shared`KMP module (`composeApp`), separating the software into layers with a strict dependency
rule: outer layers can depend on inner layers, but inner layers know nothing about the outer layers.

- **Why Clean Architecture?**

  - **Platform Independence:**The core business logic in`commonMain`is not dependent on the Android,
    iOS, or JVM frameworks, making it truly portable.

  - **Testability:**Each layer can be tested in isolation. The domain layer, containing the most
    critical business rules, can be tested with simple, fast unit tests that run on any machine.

  - **Maintainability:**The clear separation of concerns makes the codebase easier to understand,
    scale, and maintain across all platforms.

## 2. 🔄 Presentation Layer Pattern: MVI (Model-View-Intent)

The presentation layer uses a combination of a shared MVI pattern and a platform-specific UI
strategy to deliver a native experience on each platform.

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

### UI Strategy

- **Android & Desktop (Shared UI):**The Android and Desktop apps are built entirely with**Compose
  Multiplatform**. The UI is written once in the`shared`module (`composeApp`) and runs natively on
  both platforms.

- **iOS (Hybrid UI):**The iOS app uses a hybrid approach.

  - **Native SwiftUI:**The main navigation (`NavigationStack`) and specific screens that benefit
    from a strong platform feel (like the`CountryCodePicker`) are built with native SwiftUI.

  - **Shared Compose UI:**More standard, form-based screens (like`Login`and`Registration`) are
    written once in Jetpack Compose in the`shared`module and hosted within the native SwiftUI app
    using`UIViewControllerRepresentable`.

- **Why this strategy?**This approach demonstrates the flexibility of KMP. We can achieve maximum
  code reuse for Android and Desktop, while still delivering a perfectly native navigation
  experience and feel on iOS where it matters most.

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

Navigation is handled natively on each platform but is driven by logic from the shared`ViewModels`.

- **iOS:**The`iosApp`uses a SwiftUI`NavigationStack`managed by a coordinator (
  `AuthCoordinatorView`). Navigation events are triggered by callbacks passed to the shared Compose
  UI or by observing state from the shared`ViewModels`.

- **Android & Desktop:**The`androidApp`and`desktop`targets use Jetpack Compose Navigation with a
  type-safe graph defined in`AuthNavHost.kt`.

This approach ensures that navigation feels completely native on each platform while the decision of
_when_to navigate remains part of the shared business logic.

## 5. 🧪 Quality Assurance

### Testing Strategy

The project includes a suite of unit tests to ensure the logic is correct and prevent regressions.

- **Domain Layer**: UseCases are tested with pure JUnit tests in`commonTest`to verify the business
  logic.

- **Presentation Layer**: Reducers are tested to ensure state transitions are correct. ViewModels
  are tested using MockK and Turbine.

### Continuous Integration (CI)

The project includes a CI pipeline using GitHub Actions. The workflow automatically runs all unit
tests on every pull request to the`dev`branch, ensuring code quality and stability.o ensure the
integration between the UI, ViewModel, and other components works as expected.