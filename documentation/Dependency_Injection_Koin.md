# Dependency Injection with Koin in CleanEntry

This document explains how Dependency Injection (DI) is managed in the CleanEntry project using
Koin, a pragmatic lightweight dependency injection framework for Kotlin.

## Introduction

Dependency Injection is a crucial design pattern that promotes loose coupling and testability by
separating the creation of objects from their usage. CleanEntry uses Koin for its simplicity and
good KMP support.

Key goals for using Koin in this project:

* Manage dependencies for shared KMP code (ViewModels, UseCases, Repositories).
* Provide platform-specific implementations for common interfaces.
* Easy setup and integration on Android and iOS.

## Core Koin Setup: `initKoin`

The heart of the Koin setup is the `initKoin` function, located in
`composeApp/src/commonMain/kotlin/com/example/clean/entry/di/initKoin.kt`.

* **Role:** Initializes Koin with all necessary modules. It takes an optional `platformModule` to
  include platform-specific bindings and an optional `config` lambda for further Koin
  configuration (like logging on Android).
* **Modules:** It aggregates modules from different parts of the application:
    * `platformModule`: For bindings specific to Android or iOS.
    * `appModule`: Common application-level bindings (e.g., `AppNavigator`).
    * `authModule`: Bindings for the authentication feature.
    * `homeModule`: Bindings for the home/feed/details features.

```kotlin
// composeApp/src/commonMain/kotlin/com/example/clean/entry/di/initKoin.kt

fun initKoin(platformModule: Module = Module(), config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(platformModule, appModule, authModule, homeModule)
    }
}
```

## Shared and Feature Modules

Dependencies are organized into modules based on their scope and feature.

### 1. `appModule.kt`

Located in `composeApp/src/commonMain/kotlin/com/example/clean/entry/di/appModule.kt`, this module
provides application-wide dependencies.

```kotlin
// composeApp/src/commonMain/kotlin/com/example/clean/entry/di/appModule.kt

val appModule = module {
    single {
        AppNavigatorImpl(
            CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        )
    }.bind<AppNavigator>()

    viewModelOf(::MainViewModel)
}
```

### 2. `authModule.kt`

Located in `feature/auth/src/commonMain/kotlin/com/example/clean/entry/auth/di/authModule.kt`, this
module defines dependencies for the authentication feature, including data sources, repositories,
use cases, and ViewModels.

It also declares an `expect val authPlatformModule: Module`. While present, the primary mechanism
for platform-specific dependencies in CleanEntry appears to be the global `platformModule` passed to
`initKoin`. If `authPlatformModule` were actively used, it would have `actual` implementations in
the platform-specific source sets of the `feature:auth` module.

```kotlin
// feature/auth/src/commonMain/kotlin/com/example/clean/entry/auth/di/authModule.kt

expect val authPlatformModule: Module // Potentially for feature-specific platform bindings

val authModule = module {
    includes(authPlatformModule) // Includes the expected platform module

    single { get<com.example.clean.entry.db.AppDatabase>().countryEntityQueries }

    single { /* ApolloClient setup */ }
    single { /* HttpClient setup */ }

    factoryOf(::CountryRemoteDataSource)
    factoryOf(::AuthRemoteDataSource)
    factoryOf(::AuthRepositoryImpl) { bind<AuthRepository>() }

    factoryOf(::ValidateEmailUseCase)
    // ... other use cases and ViewModels ...
    viewModelOf(::LoginViewModel)
}
```

### 3. `homeModule.kt`

Located in `feature/home/src/commonMain/kotlin/com/example/clean/entry/shared/di/homeModule.kt`,
this module provides dependencies for image browsing features.

```kotlin
// feature/home/src/commonMain/kotlin/com/example/clean/entry/shared/di/homeModule.kt

val homeModule = module {
    singleOf(::ImageRepositoryImpl).bind<ImageRepository>()
    singleOf(::GetImagesUseCase)
    singleOf(::FeedViewModel)

    factoryOf(::GetImageDetailsUseCase)
    factoryOf(::GetSimilarImagesUseCase)
    viewModelOf(::DetailsViewModel)
}
```

## Handling Platform-Specific Dependencies

Koin allows providing platform-specific implementations for common interfaces. `PhoneNumberVerifier`
is a good example.

### 1. Common Interface (`PhoneNumberVerifier.kt`)

Defined in `core/src/commonMain/kotlin/com/example/clean/entry/core/util/PhoneNumberVerifier.kt`.

```kotlin
interface PhoneNumberVerifier {
    fun isValidNumber(phone: String, regionCode: String): Boolean
}
```

### 2. Android Implementation and Injection

In `composeApp/src/androidMain/kotlin/com/example/clean/entry/MainApp.kt`, the Android-specific
`PhoneNumberVerifierImpl` is provided in a `platformModule` which is then passed to `initKoin`.

```kotlin
// composeApp/src/androidMain/kotlin/com/example/clean/entry/MainApp.kt
class PhoneNumberVerifierImpl(private val phoneUtil: PhoneNumberUtil) : PhoneNumberVerifier {
    override fun isValidNumber(phone: String, regionCode: String): Boolean {
        return try {
            phoneUtil.isValidNumber(phoneUtil.parse(phone, regionCode))
        } catch (e: Exception) {
            false
        }
    }
}

val platformModule: Module = module { // Android platform-specific module
    single { PhoneNumberUtil.createInstance(androidContext()) } // libphonenumber instance
    single<PhoneNumberVerifier> { PhoneNumberVerifierImpl(get()) } // Provide actual
}

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(platformModule = platformModule) { // Pass the Android platform module
            androidLogger()
            androidContext(this@MainApp)
        }
    }
}
```

### 3. iOS Implementation and Injection

On iOS, the `actual` implementation of `PhoneNumberVerifier` (a Swift class conforming to the Kotlin
`PhoneNumberVerifier` interface/protocol) is created in Swift code. An instance is then passed to
the constructor of `DependenciesHelper` (
`composeApp/src/iosMain/kotlin/com/example/clean/entry/shared/util/DependenciesHelper.kt`).
`DependenciesHelper` then includes it in its own `platformModule` when calling `initKoin`.

```kotlin
// composeApp/src/iosMain/kotlin/com/example/clean/entry/shared/util/DependenciesHelper.kt

class DependenciesHelper : KoinComponent {
    // The Swift side instantiates its own PhoneNumberVerifier and passes it here.
    constructor(phoneNumberValidator: PhoneNumberVerifier) {
        initKoin(
            platformModule = module {
                // Provides the Swift implementation of PhoneNumberVerifier to Koin
                factory<PhoneNumberVerifier> { phoneNumberValidator }
            }
        )
    }
    // ...
}
```

*(Note: The Swift implementation of `PhoneNumberVerifier` would be defined in the iOS
project (`iosApp` or `CleanEntrySwift`) and would conform to the `PhoneNumberVerifier` protocol
generated by KMP.)*

## Initializing Koin on Each Platform

* **Android:** Koin is started in the `onCreate()` method of the `Application` class (`MainApp.kt`),
  as shown above. `androidLogger()` and `androidContext()` are Koin extensions for Android.
* **iOS:** Koin is initialized when `DependenciesHelper` is instantiated from the Swift side,
  typically early in the app lifecycle (e.g., in `AppDelegate` or when the main Swift `App` struct
  is initialized).

## Injecting and Using Dependencies

Koin makes it easy to get instances of your dependencies. Classes can extend `KoinComponent` and use
`inject()` or `get()` to retrieve dependencies. ViewModels are often injected using Koin's
ViewModel-specific helpers (e.g., `viewModelOf` in module definitions, and then resolved by
`koinViewModel()` in Compose or obtained via `get()` in `DependenciesHelper` for Swift).

```kotlin
// Example from feature/auth/src/commonMain/kotlin/com/example/clean/entry/auth/presentation/login/LoginViewModel.kt
class LoginViewModel(
    private val validateEmailUseCase: ValidateEmailUseCase, // Injected by Koin
    private val validatePasswordUseCase: ValidatePasswordUseCase, // Injected by Koin
    private val authRepository: AuthRepository, // Injected by Koin
    private val navigator: AppNavigator, // Injected by Koin
) : BaseViewModel<LoginReducer.State, LoginReducer.Event, LoginReducer.Effect>(
    reducer = LoginReducer,
    initialState = LoginReducer.State()
) {
    // ...
}
```

## Benefits in CleanEntry

* **Decoupling:** Classes don't create their dependencies; they receive them.
* **Testability:** Easier to mock dependencies in unit tests.
* **Maintainability:** Clearer understanding of the dependency graph.
* **KMP Friendly:** Koin works well across Kotlin Multiplatform targets.

This setup provides a flexible and robust way to manage dependencies throughout the CleanEntry
application.
