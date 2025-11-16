package com.pph.data.di

import com.pph.data.BuildConfig
import com.pph.data.repository.remote.api.ForecastApi
import com.pph.data.repository.remote.api.createForecastApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideBaseUrl(): String = "https://api.openweathermap.org/"

    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = false
                    isLenient = true
                })
            }
        }
    }

    @Provides
    fun providesKtorClient(
        client: HttpClient, baseUrl: String
    ): Ktorfit {
        return Ktorfit.Builder().baseUrl(baseUrl).httpClient(client).build()
    }

    @Provides
    @Singleton
    fun provideForecastApi(ktorfit: Ktorfit): ForecastApi =
        ktorfit.createForecastApi()

    @Provides
    @Named("openWeatherApiKey")
    fun provideOpenWeatherApiKey(): String = BuildConfig.OPEN_WEATHER_API_KEY
}