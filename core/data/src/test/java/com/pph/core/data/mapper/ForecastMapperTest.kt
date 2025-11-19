package com.pph.core.data.mapper

import com.pph.data.mapper.toBo
import com.pph.data.mapper.toBoList
import com.pph.data.mapper.toDailyForecastEntities
import com.pph.data.mapper.toEntity
import com.pph.data.model.dto.CurrentWeatherDto
import com.pph.data.model.dto.DailyForecastDto
import com.pph.data.model.dto.ForecastResponseDto
import com.pph.data.model.dto.TempDto
import com.pph.data.model.dto.WeatherDescriptionDto
import com.pph.data.model.entity.DailyForecastEntity
import com.pph.data.model.entity.SelectedDayEntity
import com.pph.domain.model.DailyForecastBo
import com.pph.domain.model.TempBo
import com.pph.domain.model.WeatherDescriptionBo
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class DailyForecastMapperTest {

    // Json instance for DTO (de)serialization tests
    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Test
    fun `toEntity should map all fields when weather list is not empty`() {
        // Given
        val dt = 1_700_000_000L
        val timezoneOffsetSeconds = 3600  // +1h
        val now = 123456789L

        val dto = DailyForecastDto(
            dt = dt,
            humidity = 80,
            windSpeed = 5.5,
            temp = TempDto(
                day = 21.0,
                min = 18.0,
                max = 25.0,
                night = 19.0
            ),
            weather = listOf(
                WeatherDescriptionDto(
                    id = 800,
                    main = "Clear",
                    description = "clear sky",
                    icon = "01d"
                )
            )
        )

        // When
        val entity: DailyForecastEntity = dto.toEntity(
            timezoneOffset = timezoneOffsetSeconds,
            now = now
        )

        // Then: check dateEpochDay is calculated with offset
        val expectedEpochDay = Instant.ofEpochSecond(dt)
            .atOffset(ZoneOffset.ofTotalSeconds(timezoneOffsetSeconds))
            .toLocalDate()
            .toEpochDay()

        assertEquals(expectedEpochDay, entity.dateEpochDay)
        assertEquals(dt, entity.dt)
        assertEquals(80, entity.humidity)
        assertEquals(5.5, entity.windSpeed, 0.0)

        assertEquals(21.0, entity.dayTemp, 0.0)
        assertEquals(18.0, entity.minTemp, 0.0)
        assertEquals(25.0, entity.maxTemp, 0.0)
        assertEquals(19.0, entity.nightTemp, 0.0)

        assertEquals(800, entity.weatherId)
        assertEquals("Clear", entity.weatherMain)
        assertEquals("clear sky", entity.weatherDescription)
        assertEquals("01d", entity.weatherIcon)

        assertEquals(timezoneOffsetSeconds, entity.timezoneOffset)
        assertEquals(now, entity.lastUpdated)
    }

    @Test
    fun `toEntity should use default weather values when list is empty`() {
        // Given
        val dt = 1_700_000_000L
        val timezoneOffsetSeconds = 0
        val now = 999L

        val dto = DailyForecastDto(
            dt = dt,
            humidity = 50,
            windSpeed = 3.0,
            temp = TempDto(
                day = 20.0,
                min = 15.0,
                max = 22.0,
                night = 16.0
            ),
            weather = emptyList()
        )

        // When
        val entity = dto.toEntity(
            timezoneOffset = timezoneOffsetSeconds,
            now = now
        )

        // Then: default weather values used
        assertEquals(-1, entity.weatherId)
        assertEquals("", entity.weatherMain)
        assertEquals("", entity.weatherDescription)
        assertEquals("", entity.weatherIcon)

        // And basic fields mapped correctly
        assertEquals(dt, entity.dt)
        assertEquals(timezoneOffsetSeconds, entity.timezoneOffset)
        assertEquals(now, entity.lastUpdated)
    }

    @Test
    fun `toDailyForecastEntities should map all daily items using timezoneOffset and now`() {
        // Given
        val now = 111L
        val timezoneOffsetSeconds = 7200

        val dto1 = DailyForecastDto(
            dt = 1_700_000_000L,
            humidity = 60,
            windSpeed = 4.0,
            temp = TempDto(
                day = 10.0,
                min = 8.0,
                max = 12.0,
                night = 9.0
            ),
            weather = emptyList()
        )

        val dto2 = DailyForecastDto(
            dt = 1_700_086_400L, // another day
            humidity = 70,
            windSpeed = 6.0,
            temp = TempDto(
                day = 15.0,
                min = 13.0,
                max = 18.0,
                night = 14.0
            ),
            weather = emptyList()
        )

        val response = ForecastResponseDto(
            lat = 40.0,
            lon = -3.0,
            timezone = "Europe/Madrid",
            timezoneOffset = timezoneOffsetSeconds,
            daily = listOf(dto1, dto2)
        )

        // When
        val entities: List<DailyForecastEntity> = response.toDailyForecastEntities(now)

        // Then
        assertEquals(2, entities.size)

        val first = entities[0]
        val second = entities[1]

        assertEquals(dto1.dt, first.dt)
        assertEquals(dto2.dt, second.dt)

        assertEquals(timezoneOffsetSeconds, first.timezoneOffset)
        assertEquals(timezoneOffsetSeconds, second.timezoneOffset)

        assertEquals(now, first.lastUpdated)
        assertEquals(now, second.lastUpdated)
    }

    @Test
    fun `toDailyForecastEntities should return empty list when daily list is empty`() {
        // Given
        val response = ForecastResponseDto(
            lat = 40.0,
            lon = -3.0,
            timezone = "Europe/Madrid",
            timezoneOffset = 0,
            daily = emptyList()
        )

        // When
        val entities = response.toDailyForecastEntities(now = 0L)

        // Then
        assertTrue(entities.isEmpty())
    }

    @Test
    fun `toBo should map all fields correctly`() {
        // Given: we use a full entity with all fields filled
        val entity = DailyForecastEntity(
            dateEpochDay = 20000L,
            dt = 1_700_000_000L,
            humidity = 75,
            windSpeed = 5.0,
            dayTemp = 21.5,
            minTemp = 19.0,
            maxTemp = 24.0,
            nightTemp = 20.0,
            weatherId = 500,
            weatherMain = "Rain",
            weatherDescription = "light rain",
            weatherIcon = "10d",
            timezoneOffset = 3600,
            lastUpdated = 1234L
        )

        // When
        val bo: DailyForecastBo = entity.toBo()

        // Then
        assertEquals(entity.dateEpochDay, bo.dateEpochDay)
        assertEquals(entity.dt, bo.dt)
        assertEquals(entity.humidity, bo.humidity)
        assertEquals(entity.windSpeed, bo.windSpeed, 0.0)

        assertEquals(entity.dayTemp, bo.temp.day, 0.0)
        assertEquals(entity.minTemp, bo.temp.min, 0.0)
        assertEquals(entity.maxTemp, bo.temp.max, 0.0)
        assertEquals(entity.nightTemp, bo.temp.night, 0.0)

        assertEquals(1, bo.weather.size)
        val weather: WeatherDescriptionBo = bo.weather.first()
        assertEquals(entity.weatherId, weather.id)
        assertEquals(entity.weatherMain, weather.main)
        assertEquals(entity.weatherDescription, weather.description)
        assertEquals(entity.weatherIcon, weather.icon)
    }

    @Test
    fun `toBoList should map list of entities to list of bos`() {
        // Given
        val entity1 = DailyForecastEntity(
            dateEpochDay = 20000L,
            dt = 1_700_000_000L,
            humidity = 70,
            windSpeed = 4.0,
            dayTemp = 20.0,
            minTemp = 18.0,
            maxTemp = 23.0,
            nightTemp = 19.0,
            weatherId = 800,
            weatherMain = "Clear",
            weatherDescription = "clear sky",
            weatherIcon = "01d",
            timezoneOffset = 0,
            lastUpdated = 1L
        )

        val entity2 = DailyForecastEntity(
            dateEpochDay = 20001L,
            dt = 1_700_086_400L,
            humidity = 65,
            windSpeed = 3.5,
            dayTemp = 18.0,
            minTemp = 16.0,
            maxTemp = 21.0,
            nightTemp = 17.0,
            weatherId = 801,
            weatherMain = "Clouds",
            weatherDescription = "few clouds",
            weatherIcon = "02d",
            timezoneOffset = 0,
            lastUpdated = 2L
        )

        val list = listOf(entity1, entity2)

        // When
        val boList: List<DailyForecastBo> = list.toBoList()

        // Then
        assertEquals(2, boList.size)
        assertEquals(entity1.dt, boList[0].dt)
        assertEquals(entity2.dt, boList[1].dt)
    }

    @Test
    fun `toBoList should return empty list when source list is empty`() {
        // When
        val boList: List<DailyForecastBo> = emptyList<DailyForecastEntity>().toBoList()

        // Then
        assertTrue(boList.isEmpty())
    }

    @Test
    fun `CurrentWeatherDto should deserialize wind_speed into windSpeed and weather list`() {
        // Given
        val jsonString = """
            {
              "dt": 1700000000,
              "temp": 23.5,
              "humidity": 60,
              "wind_speed": 4.2,
              "weather": [
                {
                  "id": 800,
                  "main": "Clear",
                  "description": "sunny",
                  "icon": "01d"
                }
              ]
            }
        """.trimIndent()

        // When
        val dto: CurrentWeatherDto = json.decodeFromString(jsonString)

        // Then
        assertEquals(1700000000L, dto.dt)
        assertEquals(23.5, dto.temp, 0.0)
        assertEquals(60, dto.humidity)
        assertEquals(4.2, dto.windSpeed, 0.0)

        assertEquals(1, dto.weather.size)
        val weather = dto.weather.first()
        assertEquals(800, weather.id)
        assertEquals("Clear", weather.main)
        assertEquals("sunny", weather.description)
        assertEquals("01d", weather.icon)
    }

    @Test
    fun `CurrentWeatherDto should use empty weather list when field is missing`() {
        // Given: JSON without weather field
        val jsonString = """
            {
              "dt": 1700000000,
              "temp": 20.0,
              "humidity": 55,
              "wind_speed": 3.5
            }
        """.trimIndent()

        // When
        val dto: CurrentWeatherDto = json.decodeFromString(jsonString)

        // Then
        assertEquals(1700000000L, dto.dt)
        assertEquals(20.0, dto.temp, 0.0)
        assertEquals(55, dto.humidity)
        assertEquals(3.5, dto.windSpeed, 0.0)
        assertTrue(dto.weather.isEmpty())
    }

    @Test
    fun `CurrentWeatherDto should serialize windSpeed as wind_speed`() {
        // Given
        val dto = CurrentWeatherDto(
            dt = 1700000000L,
            temp = 22.0,
            humidity = 50,
            windSpeed = 5.0,
            weather = listOf(
                WeatherDescriptionDto(
                    id = 801,
                    main = "Clouds",
                    description = "few clouds",
                    icon = "02d"
                )
            )
        )

        // When
        val serialized = json.encodeToString(dto)

        // Then: we check that windSpeed is serialized as wind_speed
        assertTrue(serialized.contains("\"wind_speed\":"))
        assertTrue(serialized.contains("5.0"))
        // And that windSpeed field name does not appear
        assertTrue(!serialized.contains("windSpeed"))
    }

    @Test
    fun `TempDto should deserialize correctly from json`() {
        // Given
        val jsonString = """
            {
              "day": 21.5,
              "min": 18.0,
              "max": 25.0,
              "night": 19.0
            }
        """.trimIndent()

        // When
        val dto: TempDto = json.decodeFromString(jsonString)

        // Then
        assertEquals(21.5, dto.day, 0.0)
        assertEquals(18.0, dto.min, 0.0)
        assertEquals(25.0, dto.max, 0.0)
        assertEquals(19.0, dto.night, 0.0)
    }

    @Test
    fun `WeatherDescriptionDto should deserialize correctly from json`() {
        // Given
        val jsonString = """
            {
              "id": 500,
              "main": "Rain",
              "description": "light rain",
              "icon": "10d"
            }
        """.trimIndent()

        // When
        val dto: WeatherDescriptionDto = json.decodeFromString(jsonString)

        // Then
        assertEquals(500, dto.id)
        assertEquals("Rain", dto.main)
        assertEquals("light rain", dto.description)
        assertEquals("10d", dto.icon)
    }

    // -----------------------------------------------------------------------
    //  SelectedDayEntity coverage
    // -----------------------------------------------------------------------

    @Test
    fun `SelectedDayEntity default id should be zero when not provided`() {
        // When
        val entity = SelectedDayEntity(selectedDayEpoch = 123456L)

        // Then
        assertEquals(0, entity.id)
        assertEquals(123456L, entity.selectedDayEpoch)
    }

    @Test
    fun `SelectedDayEntity constructor should use provided id and selectedDayEpoch`() {
        // When
        val entity = SelectedDayEntity(
            id = 42,
            selectedDayEpoch = 987654L
        )

        // Then
        assertEquals(42, entity.id)
        assertEquals(987654L, entity.selectedDayEpoch)
    }

    @Test
    fun `SelectedDayEntity copy should create new instance with updated selectedDayEpoch`() {
        // Given
        val original = SelectedDayEntity(
            id = 5,
            selectedDayEpoch = 100L
        )

        // When
        val copy = original.copy(selectedDayEpoch = 200L)

        // Then
        assertNotSame(original, copy)
        assertEquals(5, copy.id)
        assertEquals(200L, copy.selectedDayEpoch)
        assertEquals(100L, original.selectedDayEpoch)
    }

    @Test
    fun `DailyForecastBo localDate should be derived from dateEpochDay`() {
        // Given
        val epochDay = 20_000L
        val bo = DailyForecastBo(
            dateEpochDay = epochDay,
            dt = 1_700_000_000L,
            humidity = 70,
            windSpeed = 4.0,
            temp = TempBo(
                day = 20.0,
                min = 18.0,
                max = 23.0,
                night = 19.0
            ),
            weather = listOf(
                WeatherDescriptionBo(
                    id = 800,
                    main = "Clear",
                    description = "clear sky",
                    icon = "01d"
                )
            )
        )

        // When
        val localDate = bo.localDate

        // Then
        val expected = LocalDate.ofEpochDay(epochDay)
        assertEquals(expected, localDate)
    }

}