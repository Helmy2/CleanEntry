package com.example.clean.entry.feed.data

import com.example.clean.entry.feed.data.models.PexelsPhotosResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

class ImageRepositoryImpl(private val httpClient: HttpClient) : ImageRepository {
    override suspend fun getCuratedPhotos(): PexelsPhotosResponse {
        // TODO: Securely manage your API key
        val apiKey = "ANAcf7Uluv3bBl4FzKZ1uTCZgY4KtsTPBe8TLFPrfvhTXF06C4GsrC43"
        return httpClient.get("https://api.pexels.com/v1/curated?per_page=80") {
            header("Authorization", apiKey)
        }.body()
    }
}