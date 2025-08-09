# Technologies & Rationale

This document explains the "why" behind the key technologies, libraries, and patterns used in the*
*CleanEntry**project.

### 1. Core Framework: Kotlin Multiplatform (KMP)

- **What:**The foundational technology that allows us to share code across Android, iOS, and
  Desktop.

- **Why:**It enables us to write our business logic, data handling, and even UI once in Kotlin and
  reuse it everywhere. This dramatically reduces development time, eliminates inconsistencies
  between platforms, and creates a single source of truth for the app's core logic.

### 2. UI Frameworks: Compose Multiplatform & SwiftUI

- **What:**We use a hybrid UI strategy.

  - **Compose Multiplatform:**Used for the Android and Desktop UI, and for shared screens/components
    that are hosted on iOS.

  - **SwiftUI:**Used for the native iOS "shell," including navigation and specific screens that
    benefit from a strong platform-native feel.

- **Why:**This strategy showcases the flexibility of KMP. We get maximum code reuse and development
  speed for Android and Desktop, while still delivering a perfectly native navigation experience and
  feel on iOS where it matters most.

### 3. Architecture: Clean Architecture & MVI

- **What:**A combination of Clean Architecture principles for separating layers (`presentation`,
  `domain`,`data`) and the MVI pattern for a unidirectional data flow in the presentation layer.

- **Why:**This creates a highly scalable, testable, and maintainable codebase. The core logic is
  completely decoupled from the UI, making it easy to share across platforms and test in isolation.

### 4. Dependency Injection: Koin

- **What:**A pragmatic dependency injection framework for Kotlin.

- **Why:**Koin is fully compatible with KMP and provides a simple, readable DSL for defining our
  dependency graph in the`shared`module. This allows us to easily provide dependencies to our shared
  code on all platforms.

### 5. Networking: Apollo GraphQL

- **What:**A type-safe GraphQL client for Kotlin.

- **Why:**Apollo generates type-safe Kotlin models from our GraphQL schema, which eliminates an
  entire class of networking bugs. Its KMP compatibility allows us to write our networking code once
  in the`shared`module.

### 6. Database: Room (via `expect`/`actual`)

- **What:**We use an`expect`/`actual`pattern to provide a common database interface.

  - **`expect AppDatabase`:**In`commonMain`, we define the expectation of a database.

  - **`actual typealias`(Android/JVM):**On Android and Desktop, the`actual`implementation uses**Room
    **, the standard for persistence on the JVM.

  - **`actual`(iOS):**The iOS implementation would typically use a different native solution like*
    *SQLDelight**, but for this project, the pattern is established.

- **Why:**This advanced pattern allows us to use the best-in-class database library for each
  platform while still sharing the repository and data source logic.

### 7. Platform Interoperability: `expect`/`actual`

- **What:**A core KMP feature that allows us to define a common API in`commonMain`(`expect`) and
  provide platform-specific implementations in`androidMain`,`iosMain`, and`jvmMain`(`actual`).

- **Why:**This is crucial for handling platform-specific dependencies like`libphonenumber`. We can
  `expect`a phone number validator in our shared`UseCase`, and then provide the actual
  implementation using the native library on each platform, without leaking platform-specific code
  into our shared logic.