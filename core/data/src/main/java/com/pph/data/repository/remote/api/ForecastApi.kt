package com.pph.data.repository.remote.api

import com.pph.data.dto.ForecastResponseDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface ForecastApi {

    @GET("data/3.0/onecall")
    suspend fun getForecast(
        @Query("lat") lat: Double = 40.4983,
        @Query("lon") lon: Double = -3.5676,
        // We only keep current and daily, excluding minutely, hourly and alerts
        @Query("exclude") exclude: String = "minutely,hourly,alerts",
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): ForecastResponseDto
}