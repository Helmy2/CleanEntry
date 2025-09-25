package com.example.clean.entry.feed.data

import com.example.clean.entry.core.util.Secrets
import com.example.clean.entry.feed.data.models.PexelsPhotosResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

class ImageRepositoryImpl(private val httpClient: HttpClient) : ImageRepository {
    override suspend fun getCuratedPhotos(): PexelsPhotosResponse {
        val apiKey = Secrets.getPexelsApiKey()
        return httpClient.get("https://api.pexels.com/v1/curated?per_page=80") {
            header("Authorization", apiKey)
        }.body()
    }
}