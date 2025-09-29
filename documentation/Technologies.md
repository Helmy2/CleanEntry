# Technologies & Rationale

This document outlines the key technologies, libraries, and architectural patterns used in the
CleanEntry project, along with a brief rationale for their selection.

## Core Framework & Language

* **Kotlin Multiplatform (KMP)**
    * **Description:** An SDK for sharing code across different platforms, including Android, iOS,
      Desktop, Web, and server-side.
    * **Rationale:** Chosen to maximize code reuse for business logic (domain and data layers) and
      presentation logic (ViewModels, Reducers) across Android and iOS, with the potential to extend
      to other platforms. This reduces development time and ensures consistency.

* **Kotlin**
    * **Description:** A modern, concise, and safe programming language.
    * **Rationale:** The primary language for KMP. Its features like coroutines, null safety, and
      extension functions are beneficial for building robust and efficient applications.

## Architecture

* **Clean Architecture**
    * **Description:** A software architectural pattern that separates concerns into distinct
      layers (Domain, Data, Presentation), with a strict dependency rule (outer layers depend on
      inner layers).
    * **Rationale:** Promotes maintainability, testability, and scalability by decoupling business
      logic from UI and framework specifics. Allows for easier changes and independent development
      of layers.

* **MVI (Model-View-Intent)**
    * **Description:** A unidirectional data flow pattern used in the presentation layer. State is
      immutable, and changes are driven by user intents (events) processed by ViewModels and
      Reducers.
    * **Rationale:** Creates a predictable and traceable state management system, simplifying
      debugging and reasoning about UI changes. Works well with declarative UI frameworks like
      Jetpack Compose and SwiftUI.

## UI Frameworks

* **Jetpack Compose (Android)**
    * **Description:** Android’s modern declarative UI toolkit for building native interfaces.
    * **Rationale:** Enables building UIs with less code, powerful tools, and intuitive Kotlin APIs.
      Its reactive nature aligns well with the MVI pattern.

* **SwiftUI (iOS)**
    * **Description:** Apple's declarative UI framework for building apps on all Apple platforms.
    * **Rationale:** Provides a modern way to build iOS UIs. Chosen for its native integration and
      ability to interface with shared KMP ViewModels, offering a truly native experience while
      sharing presentation logic.

## Dependency Injection

* **Koin**
    * **Description:** A pragmatic and lightweight dependency injection framework for Kotlin.
    * **Rationale:** Simplifies dependency management in KMP projects, providing a straightforward
      way to declare and inject dependencies across common and platform-specific code.

## Networking

* **Ktor Client**
    * **Description:** A Kotlin-first, multiplatform asynchronous HTTP client.
    * **Rationale:** Allows for writing shared networking code in KMP for fetching data from APIs (
      like the Pexels API). Its support for coroutines and Kotlinx Serialization makes it a natural
      fit.

## Data Serialization

* **Kotlinx Serialization**
    * **Description:** A Kotlin-native library for serializing and deserializing Kotlin objects to
      and from formats like JSON.
    * **Rationale:** Provides efficient and type-safe JSON handling, seamlessly integrating with
      Ktor and KMP.

## Asynchronous Programming

* **Kotlin Coroutines**
    * **Description:** A Kotlin library for managing asynchronous operations using a sequential
      style of code.
    * **Rationale:** Simplifies asynchronous programming for network calls, database operations, and
      other background tasks, making code more readable and easier to manage compared to traditional
      callback-based approaches. Used extensively with Ktor and in ViewModels.

## Image Loading

* **Coil3 (Android - Jetpack Compose)**
    * **Description:** An image loading library for Android backed by Kotlin Coroutines. Version 3
      is KMP-ready.
    * **Rationale:** Provides an efficient and easy-to-use API for loading and displaying images
      from URLs in Jetpack Compose, with good support for caching and transformations.

* **AsyncImage (SwiftUI - iOS)**
    * **Description:** A built-in SwiftUI view for asynchronously loading and displaying images from
      URLs.
    * **Rationale:** The standard and simplest way to load remote images in SwiftUI, providing basic
      loading states.

## Build System

* **Gradle**
    * **Description:** A powerful and flexible build automation tool.
    * **Rationale:** The standard build system for Android and widely used for Kotlin Multiplatform
      projects, enabling management of dependencies and build configurations for different platforms
      and shared modules.
