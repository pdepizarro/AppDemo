package com.pph.core.data.remote

import com.pph.data.remote.safeCall
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class SafeCallTest {

    @Test
    fun `safeCall should return success when block completes successfully`() = runTest {
        // Given
        val expected = 42

        // When
        val result: Result<Int> = safeCall {
            expected
        }

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expected, result.getOrNull())
    }

    @Test
    fun `safeCall should wrap exception into failure when block throws generic error`() = runTest {
        // Given
        val exception = IllegalStateException("Something went wrong")

        // When
        val result: Result<Unit> = safeCall {
            throw exception
        }

        // Then
        assertTrue(result.isFailure)
        assertSame(exception, result.exceptionOrNull())
    }

    @Test
    fun `safeCall should rethrow CancellationException instead of wrapping it`() = runTest {
        // Given
        val cancellation = CancellationException("Job cancelled")

        try {
            // When
            safeCall<Unit> {
                throw cancellation
            }
            fail("Expected CancellationException to be thrown")
        } catch (e: CancellationException) {
            // Then
            assertSame(cancellation, e)
        }
    }
}
