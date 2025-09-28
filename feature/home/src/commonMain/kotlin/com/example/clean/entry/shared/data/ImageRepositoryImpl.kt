package com.example.clean.entry.shared.data

import com.example.clean.entry.core.util.Secrets
import com.example.clean.entry.shared.data.mapper.toDomainModel
import com.example.clean.entry.shared.data.models.PexelsPhoto
import com.example.clean.entry.shared.data.models.PexelsPhotosResponse
import com.example.clean.entry.shared.domain.model.Image
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.encodeURLQueryComponent

class ImageRepositoryImpl(private val httpClient: HttpClient) : ImageRepository {
    override suspend fun getCuratedPhotos(): PexelsPhotosResponse {
        val apiKey = Secrets.getPexelsApiKey()
        return httpClient.get("https://api.pexels.com/v1/curated?per_page=80") {
            header("Authorization", apiKey)
        }.body()
    }

    override suspend fun getImageById(id: Long): Result<Image> {
        val apiKey = Secrets.getPexelsApiKey()
        return try {
            // Use PexelsPhoto (from PexelsPhotosResponse.kt) which has nullable fields
            val pexelsPhoto = httpClient.get("https://api.pexels.com/v1/photos/$id") {
                header("Authorization", apiKey)
            }.body<PexelsPhoto>() // Changed from PexelsPhotoDto
            val uiImage = pexelsPhoto.toDomainModel()
            Result.success(uiImage)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchPhotosByQuery(
        query: String,
        page: Int,
        perPage: Int
    ): Result<List<Image>> {
        val apiKey = Secrets.getPexelsApiKey()
        val encodedQuery = query.encodeURLQueryComponent()
        return try {
            val response =
                httpClient.get("https://api.pexels.com/v1/search?query=$encodedQuery&page=$page&per_page=$perPage") {
                    header("Authorization", apiKey)
                }.body<PexelsPhotosResponse>()

            val similarImages = response.photos.map { photo -> // photo is of type PexelsPhoto
                photo.toDomainModel()
            }
            Result.success(similarImages)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
