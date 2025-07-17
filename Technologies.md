# Technologies & Rationale

This document explains the "why" behind the key technologies, libraries, and patterns used in the  **CleanEntry**  project. Each choice was made to support the project's goals of being modern, scalable, testable, and maintainable.

### 1. Core Language: Kotlin

- **What:**  The exclusive programming language for the project.

- **Why:**

    - **Official & Modern:**  Kotlin is Google's official language for Android development. It provides modern language features like null safety, data classes, and extension functions that lead to safer and more concise code.

    - **Coroutines & Flow:**  Built-in support for coroutines and Flow makes asynchronous programming (like network calls or database access) simple, efficient, and readable, which is fundamental to a responsive UI.


### 2. UI Toolkit: Jetpack Compose

- **What:**  Android's modern, declarative UI toolkit used for the entire UI layer.

- **Why:**

    - **Declarative Mindset:**  You describe  _what_  the UI should look like for a given state, and Compose handles the rendering. This is more intuitive and less error-prone than the old imperative way (XML) of manually manipulating views.

    - **Less Code, Faster Development:**  It requires significantly less code than the XML-based system, which means faster development, fewer bugs, and easier maintenance.

    - **Direct Integration:**  Because it's written in Kotlin, your UI code and business logic share the same language, creating a seamless development experience.


### 3. Architecture: Clean Architecture

- **What:**  A set of principles that separates the project into distinct layers (`presentation`,  `domain`,  `data`).

- **Why:**

    - **Separation of Concerns:**  Each layer has a single, clear responsibility. This separation is the key to a scalable and maintainable codebase.

    - **Testability:**  The  `domain`  layer, containing core business logic (like validation  `UseCases`), is pure Kotlin and can be tested without needing an Android device. The  `data`  and  `presentation`  layers can also be tested in isolation.

    - **Maintainability:**  This structure allows individual parts of the app to be modified or replaced without affecting other parts. For example, the UI can be completely redesigned without touching the business logic.


### 4. Presentation Pattern: MVI (Model-View-Intent)

- **What:**  A reactive, unidirectional data flow pattern used within the presentation layer.

- **Why:**

    - **Predictable State:**  MVI enforces a strict cycle: a user action (`Intent`/`Event`) is processed, which produces a new  `State`, which is then rendered by the UI. This makes the state of the app predictable and easy to debug.

    - **Single Source of Truth:**  The UI is a simple function of the  `State`. There is only one source of truth, which eliminates a whole category of state-related bugs.

    - **Separation of Logic:**  We use a  `Reducer`  object to handle pure state changes, which keeps the  `ViewModel`focused on handling business logic and side effects.


### 5. Dependency Injection: Koin

- **What:**  A pragmatic dependency injection framework for Kotlin.

- **Why:**

    - **Simplicity & Readability:**  Koin uses a simple Kotlin DSL (Domain-Specific Language) to declare dependencies in modules, which is often considered more straightforward and easier to read than annotation-based alternatives.

    - **No Annotation Processing:**  Unlike Dagger/Hilt, Koin does not use annotation processing, which can contribute to faster build times.

    - **Multiplatform Ready:**  Koin is a popular choice for Kotlin Multiplatform (KMP) projects, keeping the architecture flexible for future expansion.


### 6. Navigation: Jetpack Compose Navigation

- **What:**  The official library for handling navigation within a Compose application.

- **Why:**

    - **Type Safety:**  By defining our navigation routes in a  `sealed class`  (`AuthDestination.kt`), we make our navigation graph completely type-safe, eliminating runtime errors from string-based typos.

    - **Feature-Owned Graphs:**  Each feature module (like  `:feature_auth`) defines its own  `NavHost`. This makes features self-contained and reusable. The main  `:app`  module simply includes these feature graphs.

    - **State Handling:**  It provides robust mechanisms like the  `SavedStateHandle`  for passing data and returning results between screens in a lifecycle-aware manner.


### 7. Phone Validation:  `libphonenumber-android`

- **What:**  Google's official library for parsing, formatting, and validating international phone numbers.

- **Why:**

    - **Reliability & Accuracy:**  Phone number validation is notoriously complex due to varying international formats. Using Google's library is the industry standard and ensures our validation logic is correct and comprehensive.

    - **Separation of Concerns:**  The validation logic is encapsulated within a dedicated  `ValidatePhoneUseCase`, keeping this complex dependency isolated within our domain layer.