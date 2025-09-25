package com.example.clean.entry.feed.di

import com.example.clean.entry.feed.data.ImageRepository
import com.example.clean.entry.feed.data.ImageRepositoryImpl
import com.example.clean.entry.feed.domain.usecase.GetImagesUseCase
import com.example.clean.entry.feed.presentation.FeedViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val feedModule = module {
    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
    singleOf(::ImageRepositoryImpl).bind<ImageRepository>()
    singleOf(::GetImagesUseCase)
    singleOf(::FeedViewModel)
}