package com.example.clean.entry.home.di

import com.example.clean.entry.home.data.ImageRepository
import com.example.clean.entry.home.data.ImageRepositoryImpl
import com.example.clean.entry.home.domain.usecase.GetImagesUseCase
import com.example.clean.entry.home.presentation.HomeViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeModule = module {
    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
    singleOf(::ImageRepositoryImpl).bind<ImageRepository>()
    singleOf(::GetImagesUseCase)
    singleOf(::HomeViewModel)
}