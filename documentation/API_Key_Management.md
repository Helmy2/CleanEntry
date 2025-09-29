# API Key Management in CleanEntry

This document outlines the strategy used in CleanEntry to manage sensitive API keys, such as those
for Pexels and Firebase. The approach aims to avoid hardcoding keys directly in easily accessible
Kotlin, Swift, or Android Java/Kotlin source files by leveraging Kotlin Multiplatform's `expect`/
`actual` mechanism along with native C/C++ code.

## Introduction

Storing API keys directly in your shared application code can pose a security risk, as they can be
more easily extracted from the application package. While no client-side storage method is perfectly
secure, the method used in CleanEntry adds a layer of obfuscation by embedding keys in compiled
native code.

The core idea is:

1. Define an `expect` object in common Kotlin code that declares functions to retrieve API keys.
2. Provide `actual` implementations for Android and iOS that fetch these keys from native C/C++
   code.

## KMP `expect` Declaration

The common API for accessing secrets is defined in
`core/src/commonMain/kotlin/com/example/clean/entry/core/util/Secrets.kt`.

* **Role:** This `expect object` declares the functions that platform-specific code will implement
  to provide the actual keys.
* Any shared KMP module needing an API key will call these functions.

```kotlin
// core/src/commonMain/kotlin/com/example/clean/entry/core/util/Secrets.kt

expect object Secrets {
    fun getPexelsApiKey(): String
    fun getFirebaseApiKey(): String
}
```

## Android Implementation

On Android, the API keys are stored in C++ code, compiled into a native library using the NDK, and
accessed via JNI.

### 1. `Secrets.android.kt` (Actual Implementation)

This file provides the `actual` implementation for the `Secrets` object on Android.

* **Role:** Bridges the Kotlin world to the native C++ world.
* It declares `external fun` for each key-retrieval function, which corresponds to a JNI function
  implemented in C++.
* It loads the native shared library (e.g., `native-lib`) using `System.loadLibrary()`.

```kotlin
// core/src/androidMain/kotlin/com/example/clean/entry/core/util/Secrets.android.kt

actual object Secrets {

    init {
        System.loadLibrary("native-lib") // Matches library name in CMakeLists.txt
    }

    private external fun getPexelsApiKeyFromNative(): String
    private external fun getFirebaseApiKeyFromNative(): String

    actual fun getPexelsApiKey(): String {
        return getPexelsApiKeyFromNative()
    }

    actual fun getFirebaseApiKey(): String {
        return getFirebaseApiKeyFromNative()
    }
}
```

### 2. C++ Code (`native-lib.cpp`)

The actual API keys for Android are stored as string literals within C++ functions.

* **Role:** Holds the secret strings. The JNI function names must match a specific pattern:
  `Java_packageName_ClassName_methodName`.

```cpp
// core/src/cpp/native-lib.cpp
#include
<jni.h>
#include
<string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_clean_entry_core_util_Secrets_getPexelsApiKeyFromNative(
JNIEnv* env,
jobject /* this */) {
std::string apiKey = "..."; // Pexels API Key
return env->NewStringUTF(apiKey.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_clean_entry_core_util_Secrets_getFirebaseApiKeyFromNative(
JNIEnv* env,
jobject /* this */) {
std::string apiKey = "..."; // Firebase API Key
return env->NewStringUTF(apiKey.c_str());
}
```

### 3. CMake (`CMakeLists.txt`)

The `CMakeLists.txt` file is responsible for configuring how the C++ code is built into a shared
library.

* **Role:** Defines the name of the library (e.g., `native-lib`) that Kotlin code will load.

```cmake
# core/src/cpp/CMakeLists.txt
# ...
add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        native-lib.cpp)
# ...
```

## iOS Implementation

On iOS, API keys are stored in C functions defined in a header file, and accessed using
Kotlin/Native's C interop capabilities.

### 1. `Secrets.ios.kt` (Actual Implementation)

This file provides the `actual` implementation for the `Secrets` object on iOS.

* **Role:** Bridges Kotlin to C functions.
* It uses `@OptIn(ExperimentalForeignApi::class)` and Kotlin/Native's interop functions (like
  `toKString()`) to call the C functions and convert the results.

```kotlin
// core/src/iosMain/kotlin/com/example/clean/entry/core/util/Secrets.ios.kt

@OptIn(ExperimentalForeignApi::class)
actual object Secrets {
    actual fun getPexelsApiKey(): String =
        getPexelsApiKeyFromNative()?.toKString() ?: ""

    actual fun getFirebaseApiKey(): String =
        getFirebaseApiKeyFromNative()?.toKString() ?: ""
}
```

### 2. C Header (`Secrets.h`)

The API keys for iOS are stored as string literals returned by C functions in a header file.

* **Role:** Holds the secret strings for the iOS platform. This file is typically placed in a
  directory that Kotlin/Native's cinterop process can access (e.g., `src/nativeInterop/cinterop/`).

```c
// core/src/nativeInterop/cinterop/Secrets.h
#ifndef
SECRETS_H
#define
SECRETS_H

const char* getFirebaseApiKeyFromNative(void){
return "..."; // Firebase API Key
};
const char* getPexelsApiKeyFromNative(void){
return "..."; // Pexels API Key
}

#endif
```

The Kotlin/Native compiler and the cinterop tool are configured (usually in the `build.gradle.kts`
of the module containing this code) to process this header file, making its functions available to
Kotlin.

## Usage in KMP Code

Once set up, shared KMP code can simply call the functions on the `Secrets` object to retrieve the
keys:

```kotlin
// Example usage in a shared KMP module (e.g., when setting up Ktor client)
val pexelsApiKey = Secrets.getPexelsApiKey()
val firebaseApiKey = Secrets.getFirebaseApiKey()

ktorClient = HttpClient {
    install(ContentNegotiation) { json() }
    defaultRequest {
        header("Authorization", pexelsApiKey)
    }
}
```

## Security Considerations & Limitations

* **Basic Obfuscation:** This method primarily serves to move keys out of easily decompilable
  Kotlin/Java bytecode and into native binaries, which are somewhat harder to reverse-engineer but
  not impossible.
* **Keys in App Package:** The API keys are still embedded within the compiled application package (
  APK for Android, IPA for iOS). Determined individuals can still extract them.
* **Not Foolproof:** This is not a high-security solution. It's a trade-off between convenience in a
  KMP setup and improved (but not perfect) security over plain hardcoding in shared code.
* **Server-Side Recommended for High Security:** For truly sensitive operations or to protect keys
  more robustly, it's best to have your application communicate with a backend server that you
  control. This server can then securely store the API keys and make requests to third-party
  services on behalf of the client app.
* **Restricted Key Scopes:** Whenever possible, configure your API keys on the service provider's
  dashboard to have the minimum necessary permissions and to be restricted (e.g., to specific bundle
  IDs or application signatures) to limit potential abuse if a key is compromised.

This `expect`/`actual` pattern combined with native code provides a practical way to handle API keys
in CleanEntry, offering a better approach than directly embedding them in shared Kotlin files.
