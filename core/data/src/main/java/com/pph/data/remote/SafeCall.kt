package com.pph.data.remote

import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <T> safeCall(
    crossinline block: suspend () -> T
): Result<T> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}