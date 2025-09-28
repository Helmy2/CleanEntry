package com.example.clean.entry.shared.data

import com.example.clean.entry.shared.data.models.PexelsPhotosResponse
import com.example.clean.entry.shared.domain.model.Image

interface ImageRepository {
    suspend fun getCuratedPhotos(): PexelsPhotosResponse
    suspend fun getImageById(id: Long): Result<Image>
    suspend fun searchPhotosByQuery(
        query: String,
        page: Int,
        perPage: Int
    ): Result<List<Image>>
}