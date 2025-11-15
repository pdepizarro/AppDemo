package com.pph.criminalitis.data

import com.pph.data.repository.remote.RoverApiImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

import io.ktor.client.*

class RoverApiImplTest {

    private val dummyHttpClient = HttpClient()
    private val roverApi = com.pph.data.repository.remote.RoverApiImpl(dummyHttpClient)

    @Test
    fun `GIVEN input with simple commands WHEN updating rover THEN returns expected final position`() = runTest {
        val input = RoverInputBo(
            topRightCorner = CoordinateBo(5, 5),
            roverPosition = CoordinateBo(1, 2),
            roverDirection = DirectionBo.N,
            movements = "LMLMLMLMM"
        )

        val result = roverApi.updateRoverPosition(input)

        assertEquals("1 3 N", result.result)
    }

    @Test
    fun `GIVEN movement that causes wrap around WHEN updating rover THEN it wraps correctly`() = runTest {
        val input = RoverInputBo(
            topRightCorner = CoordinateBo(5, 5),
            roverPosition = CoordinateBo(5, 5),
            roverDirection = DirectionBo.E,
            movements = "M" // goes beyond the edge
        )

        val result = roverApi.updateRoverPosition(input)

        assertEquals("1 5 E", result.result)
    }

    @Test
    fun `GIVEN invalid command WHEN updating rover THEN it throws exception`() = runTest {
        val input = RoverInputBo(
            topRightCorner = CoordinateBo(5, 5),
            roverPosition = CoordinateBo(1, 1),
            roverDirection = DirectionBo.N,
            movements = "MX" // X is invalid
        )

        val exception = kotlin.runCatching {
            roverApi.updateRoverPosition(input)
        }.exceptionOrNull()

        // Check type and message
        assert(exception is IllegalArgumentException)
        assertEquals("Invalid command: X", exception?.message)
    }
}