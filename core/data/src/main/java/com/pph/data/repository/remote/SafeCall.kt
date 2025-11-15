package com.pph.data.repository.remote

import kotlin.coroutines.cancellation.CancellationException

inline fun <T> safeCall(block: () -> T): Result<T> {
    return runCatching {
        block()
    }.onFailure { e ->
        if(e is CancellationException) throw e
    }
}