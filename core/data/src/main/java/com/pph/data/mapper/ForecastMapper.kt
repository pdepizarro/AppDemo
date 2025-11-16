package com.pph.data.mapper

import com.pph.data.dto.CurrentWeatherDto
import com.pph.data.dto.DailyForecastDto
import com.pph.data.dto.ForecastResponseDto
import com.pph.data.dto.TempDto
import com.pph.data.dto.WeatherDescriptionDto
import com.pph.domain.model.CurrentWeatherBo
import com.pph.domain.model.DailyForecastBo
import com.pph.domain.model.ForecastResponseBo
import com.pph.domain.model.TempBo
import com.pph.domain.model.WeatherDescriptionBo
import kotlin.collections.map

fun ForecastResponseDto.toBo(): ForecastResponseBo = ForecastResponseBo(
    lat = this.lat,
    lon = this.lon,
    timezone = this.timezone,
    timezoneOffset = this.timezoneOffset,
    current = this.current?.toBo(),
    daily = this.daily.map { it.toBo() }
)

fun CurrentWeatherDto.toBo(): CurrentWeatherBo = CurrentWeatherBo(
    dt = this.dt,
    temp = this.temp,
    humidity = this.humidity,
    windSpeed = this.windSpeed,
    weather = this.weather.map { it.toBo() }
)

fun DailyForecastDto.toBo(): DailyForecastBo = DailyForecastBo(
    dt = this.dt,
    temp = this.temp.toBo(),
    humidity = this.humidity,
    windSpeed = this.windSpeed,
    weather = this.weather.map { it.toBo() }
)

fun TempDto.toBo(): TempBo = TempBo(
    day = this.day,
    min = this.min,
    max = this.max,
    night = this.night
)

fun WeatherDescriptionDto.toBo(): WeatherDescriptionBo = WeatherDescriptionBo(
    id = this.id,
    main = this.main,
    description = this.description,
    icon = this.icon
)


