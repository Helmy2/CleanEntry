package com.example.clean.entry.core.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

/**
 * A reusable helper function that executes a given suspending block of code
 * on the IO dispatcher and wraps the result in a Kotlin `Result` object.
 *
 * This function centralizes the logic for thread switching and exception handling
 * for all data layer operations.
 *
 * @param T The type of the successful result.
 * @param block The suspending lambda to execute.
 * @return A `Result<T>` which is either a `Result.success(value)` or `Result.failure(exception)`.
 */
suspend fun <T> runCatchingOnIO(block: suspend () -> T): Result<T> {
    return withContext(Dispatchers.IO) {
        try {
            Result.success(block())
        } catch (e: Exception) {
            // Centralized logging for all data-layer errors can be added here.
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
