# Navigation in CleanEntry

This document details the navigation system implemented in the CleanEntry project. It's designed to
centralize navigation logic within the Kotlin Multiplatform (KMP) shared code while leveraging
native navigation capabilities on Android (Jetpack Compose) and iOS (SwiftUI).

## Introduction

The primary goals of CleanEntry's navigation architecture are:

* **Shared Navigation Logic:** Define navigation paths and trigger navigation events from shared KMP
  ViewModels.
* **Native Execution:** Utilize platform-specific navigation components (Jetpack Navigation for
  Android, SwiftUI Navigation for iOS) for the actual UI transitions, ensuring a native look and
  feel.
* **Decoupling:** Keep ViewModels unaware of platform-specific navigation details.

## KMP Navigation Core

The core components for navigation are defined in the `core` KMP module.

### 1. `AppDestination.kt`

This sealed class defines all possible navigation destinations within the application. Each
destination can be a simple object or a data class carrying parameters. They are marked
`@Serializable` to support type-safe navigation in Jetpack Compose.

```kotlin
// Snippet from core/src/commonMain/kotlin/com/example/clean/entry/core/navigation/AppDestination.kt

sealed class AppDestination {
    @Serializable
    object Feed : AppDestination()

    @Serializable
    object Profile : AppDestination()

    @Serializable
    object Auth : AppDestination() // Could be a root for auth flow

    @Serializable
    object Login : AppDestination()

    @Serializable
    object Registration : AppDestination()

    @Serializable
    data class CountryCodePicker(val code: String?) : AppDestination()

    @Serializable
    data class ImageDetails(val imageId: Long) : AppDestination()
}
```

### 2. `Command.kt`

This sealed class defines the types of navigation actions that can be performed. These commands are
emitted by the `AppNavigator` and observed by the platform-specific UI layers.

```kotlin
// Snippet from core/src/commonMain/kotlin/com/example/clean/entry/core/navigation/Command.kt

sealed class Command {
    data class NavigateTo(val destination: AppDestination) : Command()
    data object NavigateBack : Command()
    data class NavigateAsRoot(val destination: AppDestination) : Command()
}
```

### 3. `AppNavigator.kt`

This interface defines the contract for navigation. ViewModels use an instance of `AppNavigator` to
request navigation changes. It exposes a `Flow` of `Command` objects that the UI layer collects.

```kotlin
// Snippet from core/src/commonMain/kotlin/com/example/clean/entry/core/navigation/AppNavigator.kt

interface AppNavigator {
    val commands: Flow<Command> // Platform UI collects this

    fun navigateAsRoot(destination: AppDestination)
    fun navigateBack()
    fun navigate(destination: AppDestination)

    // For navigation with results (if used)
    fun <T : NavigationSavedResult> getResultValue(key: String): Flow<T?>
    fun navigateBackWithResult(returnResult: NavigationSavedResult)
}
```

An actual implementation of `AppNavigator` (e.g., `AppNavigatorImpl`) would manage a `Channel` or
`SharedFlow` to emit these commands.

### 4. ViewModel Example: `LoginViewModel.kt`

ViewModels trigger navigation by calling methods on an injected `AppNavigator` instance.

```kotlin
// Snippet from feature/auth/src/commonMain/kotlin/com/example/clean/entry/auth/presentation/login/LoginViewModel.kt
class LoginViewModel(
    // ... other dependencies
    private val navigator: AppNavigator,
) : BaseViewModel<LoginReducer.State, LoginReducer.Event, LoginReducer.Effect>(
    // ...
) {
    override fun handleEvent(event: LoginReducer.Event) {
        when (event) {
            is LoginReducer.Event.CreateAccountClicked -> {
                // Example of navigating to a new destination
                navigator.navigate(AppDestination.Registration)
            }
            is LoginReducer.Event.LoginSuccess -> {
                // Example of navigating to a new root
                navigator.navigateAsRoot(AppDestination.Feed)
            }
            // ... other event handling
        }
    }
    // ...
}
```

## Android (Jetpack Compose) Implementation

On Android, navigation is handled primarily by `AppNavHost.kt` using Jetpack Navigation Compose.

### `AppNavHost.kt`

This Composable function is responsible for:

* **Setting up `NavController`:** Uses `rememberNavController()`.
* **Obtaining `AppNavigator`:** Injects the KMP `AppNavigator` using Koin (
  `koinInject<AppNavigator>()`).
* **Collecting Commands:** Uses a `LaunchedEffect` to collect the `navigator.commands` Flow.
* **Translating Commands:** When a `Command` is received, it's translated into actions on the
  Jetpack `NavController` (e.g., `navController.navigate(command.destination)`,
  `navController.popBackStack()`).
* **Building Navigation Graph:** Uses `NavHost` and the `composable` builder function with KMP
  `AppDestination` (which are Kotlin `Serializable` objects/classes) to define the navigation graph.
* **Handling Bottom Navigation/Navigation Rail:** It also includes logic for bottom navigation and
  navigation rail based on screen size, navigating to `AppDestination`s like `Feed` and `Profile`.

```kotlin
// Snippet from composeApp/src/commonMain/kotlin/com/example/clean/entry/navigation/AppNavHost.kt

@Composable
fun AppNavHost(
    startDestination: AppDestination,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val navigator = koinInject<AppNavigator>() // KMP AppNavigator

    // Collect commands from KMP AppNavigator
    LaunchedEffect(navigator.commands) {
        navigator.commands.collect { command ->
            when (command) {
                is Command.NavigateAsRoot -> {
                    navController.navigate(command.destination) {
                        popUpTo(0) // Clears the back stack
                    }
                }
                Command.NavigateBack -> navController.popBackStack()
                is Command.NavigateTo -> navController.navigate(command.destination)
            }
        }
    }

    // ... (logic for Scaffold, BottomNavigationBar/NavigationRail based on screen size) ...

    // The NavHost where composable destinations are defined
    NavHost(
        navController = navController,
        startDestination = startDestination, // Initial screen
        modifier = modifier.padding(innerPadding) // If using Scaffold
    ) {
        authNavBuilder() // For auth-related screens like Login, Registration

        composable<AppDestination.Feed> {
            FeedRoute()
        }
        composable<AppDestination.Profile> {
            ProfileRoute()
        }
        composable<AppDestination.ImageDetails> { backStackEntry ->
            val routeArgs = backStackEntry.toRoute<AppDestination.ImageDetails>()
            ImageDetailsScreen(routeArgs.imageId)
        }
        // ... other destinations
    }
}
```

## iOS (SwiftUI) Implementation

On iOS, navigation is coordinated by `NavigatorHelper.swift` which observes the KMP `AppNavigator`
and drives SwiftUI's navigation tools.

### 1. `DependenciesHelper.kt` (KMP `iosMain`)

This KMP class (in `composeApp/src/iosMain/...`) initializes Koin for iOS and provides access to
shared KMP instances, including the `AppNavigator`.

```kotlin
// Snippet from composeApp/src/iosMain/kotlin/com/example/clean/entry/shared/util/DependenciesHelper.kt
class DependenciesHelper : KoinComponent {
    // ...
    val navigator: AppNavigator by inject() // Provides KMP AppNavigator
    // ...
}
```

This is typically accessed via a singleton like `iOSApp.dependenciesHelper` in Swift.

### 2. `NavigatorHelper.swift`

This `ObservableObject` is the bridge between the KMP `AppNavigator` and SwiftUI's navigation
system.

* **Obtains `AppNavigator`:** Gets the KMP `AppNavigator` instance from `DependenciesHelper`.
* **Subscribes to Commands:** Collects the `commands` Flow from the KMP `AppNavigator`.
* **Translates Commands & Destinations:** Converts KMP `Command` and `AppDestination` objects into
  Swift-specific enums (`AppCommand`, `AppDestination` in Swift) for easier use in SwiftUI.
* **Manages SwiftUI State:** Publishes `@Published` properties like `navigationPath` (for
  `NavigationStack`), `selectedTab` (for `TabView`), and the translated `command`.
* **Handles Navigation Logic:** The `handleNavigationCommand(_:)` function updates the
  `navigationPath` or `selectedTab` based on the received command.

```swift
// Snippet from iosApp/iosApp/Core/Navigation/NavigatorHelper.swift

import Foundation
import SwiftUI
import shared // KMP shared module
import Combine

// Swift-specific AppDestination enum, mirroring KMP AppDestination
enum AppDestination: Hashable, Identifiable { // Identifiable for TabView, Hashable for NavigationStack
    case feed, profile, login, registration
    case countryCodePicker(selectedCountryCode: String?)
    case details(imageId: Int64)
    // ...

    var id: String {
        // ... provide a unique ID for each case
        switch self {
        case .feed: "feed"
            // ... etc.
        case .details(let imageId): "details-\(imageId)"
        }
    }
}

// Swift-specific AppCommand enum
enum AppCommand {
    case navigateTo(destination: AppDestination)
    case navigateBack
    case navigateAsRoot(destination: AppDestination)
}

@MainActor
class NavigatorHelper: ObservableObject {
    @Published private(set) var command: AppCommand?
    @Published var selectedTab: AppDestination = .login // Default tab
    @Published var navigationPath = NavigationPath()

    private let kmpNavigator = iOSApp.dependenciesHelper.navigator // KMP AppNavigator
    private var task: Task<Void, Never>?

    init() {
        startObservingKMPCommands()
    }

    private func startObservingKMPCommands() {
        task = Task {
            // ... (optional: set initial tab based on auth state) ...
            for await kmpCmd in kmpNavigator.commands { // Collect KMP commands
                if Task.isCancelled {
                    break
                }
                self.command = translateKMPCommand(kmpCmd) // Translate and publish
            }
        }
    }

    func handleNavigationCommand(_ command: AppCommand?) {
        switch command {
        case .navigateBack:
            if !navigationPath.isEmpty {
                navigationPath.removeLast()
            }
        case .navigateAsRoot(let destination):
            if destination == .feed || destination == .profile { // Tab destinations
                selectedTab = destination
                navigationPath = NavigationPath() // Clear path for root tab change
            } else if destination == .login { // Auth root
                selectedTab = .login
                navigationPath = NavigationPath()
            } else { // Other destinations as root of stack
                navigationPath = NavigationPath([destination])
            }
        case .navigateTo(let destination):
            if destination == .feed || destination == .profile { // Switch tab
                selectedTab = destination
            } else { // Push to navigation stack
                navigationPath.append(destination)
            }
        case .none:
            break
        }
    }

    // Helper to translate KMP AppDestination to Swift AppDestination
    private func translateKMPDestination(_ kmpDestination: shared.AppDestination) -> AppDestination {
        switch kmpDestination {
        case is AppDestination.Feed: return .feed
        case is AppDestination.Profile: return .profile
            // ... other mappings ...
        case let details as AppDestination.ImageDetails: return .details(imageId: details.imageId)
        default: return .login // Fallback
        }
    }

    // Helper to translate KMP Command to Swift AppCommand
    private func translateKMPCommand(_ kmpCommand: shared.Command) -> AppCommand? {
        switch kmpCommand {
        case let cmd as Command.NavigateTo:
            return .navigateTo(destination: translateKMPDestination(cmd.destination))
        case is Command.NavigateBack:
            return .navigateBack
        case let cmd as Command.NavigateAsRoot:
            return .navigateAsRoot(destination: translateKMPDestination(cmd.destination))
        default: return nil
        }
    }

    // ...
}
```

### 3. `AppCoordinatorView.swift` (SwiftUI)

This SwiftUI `View` sets up the main navigation structure (like `NavigationStack` and `TabView`) and
uses `NavigatorHelper` to drive UI changes.

* **Instantiates `NavigatorHelper`:** Uses `@StateObject` to keep `NavigatorHelper` alive.
* **`NavigationStack`:** Binds its `path` to `navigatorHelper.navigationPath`.
* **`TabView`:** Binds its `selection` to `navigatorHelper.selectedTab`.
* **`.navigationDestination(for:)`:** Uses this modifier to define which SwiftUI `View` to show for
  each Swift `AppDestination` type when it's pushed onto the `navigationPath`.
* **`.onReceive`:** Observes `navigatorHelper.$command` to call
  `navigatorHelper.handleNavigationCommand(command)` when a new (translated) command is published by
  the helper.

```swift
// Snippet from iosApp/iosApp/Core/Navigation/AppCoordinatorView.swift

import SwiftUI
import shared

struct AppCoordinatorView: View {
    @StateObject private var navigatorHelper = NavigatorHelper()

    var body: some View {
        NavigationStack(path: $navigatorHelper.navigationPath) { // Path driven by helper
            Group { // Main content: Login or TabView
                switch navigatorHelper.selectedTab {
                case .login:
                    LoginView() // Assuming LoginView is defined
                default: // For .feed, .profile, etc.
                    MainTabView(navigatorHelper: navigatorHelper)
                }
            }
            .navigationDestination(for: AppDestination.self) { destination in
                // Handles pushes
                switch destination {
                case .registration:
                    RegistrationView() // Assuming RegistrationView is defined
                case .countryCodePicker(let code):
                    CountryPickerView(helper: CountryPickerViewModelHelper(code))
                case .details(let imageId):
                    DetailsView(helper: DetailsViewModelHelper(imageId))
                default:
                    EmptyView() // Fallback for unhandled destinations in stack
                }
            }
        }
        .onReceive(navigatorHelper.$command) { command in
            // React to new commands
            navigatorHelper.handleNavigationCommand(command)
        }
    }
}

struct MainTabView: View {
    @ObservedObject var navigatorHelper: NavigatorHelper // Passed from AppCoordinatorView

    var body: some View {
        TabView(selection: $navigatorHelper.selectedTab) { // Selection driven by helper
            FeedView() // Assuming FeedView is defined
            .tabItem {
                Label("Feed", systemImage: "list.bullet")
            }
            .tag(AppDestination.feed)

            ProfileView() // Assuming ProfileView is defined
            .tabItem {
                Label("Profile", systemImage: "person.crop.circle")
            }
            .tag(AppDestination.profile)
        }
    }
}
```

## Overall Flow Summary

1. **KMP ViewModel:** A ViewModel (e.g., `LoginViewModel`) decides to navigate and calls a method on
   the injected `AppNavigator` (e.g., `navigator.navigate(AppDestination.Registration)`).
2. **KMP `AppNavigator`:** The `AppNavigator` implementation emits a `Command` (e.g.,
   `Command.NavigateTo(AppDestination.Registration)`) into its `commands` Flow.
3. **Platform Collectors:**
    * **Android (`AppNavHost.kt`):** The `LaunchedEffect` collects this `Command`. It uses the
      Jetpack `NavController` to perform the navigation (e.g.,
      `navController.navigate(AppDestination.Registration)`). The `NavHost` displays the
      corresponding Composable.
    * **iOS (`NavigatorHelper.swift`):** The `Task` collecting `kmpNavigator.commands` receives the
      KMP `Command`. It translates it into a Swift `AppCommand` and updates its
      `@Published var command`.
4. **SwiftUI Update (`AppCoordinatorView.swift`):**
    * The `.onReceive(navigatorHelper.$command)` modifier in `AppCoordinatorView` triggers.
    * `navigatorHelper.handleNavigationCommand(command)` is called.
    * `NavigatorHelper` updates its `@Published var navigationPath` (e.g., appends the translated
      Swift `AppDestination.registration`).
    * SwiftUI's `NavigationStack` observes this change to `navigationPath` and displays the `View`
      associated with `AppDestination.registration` via the `.navigationDestination` modifier. Tab
      changes are handled similarly by updating `selectedTab`.

## Benefits

* **Shared Logic:** Navigation decisions and destinations are defined once in KMP.
* **Native Experience:** Uses standard platform navigation components for a familiar UX.
* **Testability:** ViewModel navigation logic can be unit tested by mocking `AppNavigator`. KMP
  navigation commands themselves can be tested.
* **Decoupling:** ViewModels don't need to know about `NavController` or `NavigationStack`.

This architecture provides a robust and maintainable way to handle navigation in a Kotlin
Multiplatform application.
