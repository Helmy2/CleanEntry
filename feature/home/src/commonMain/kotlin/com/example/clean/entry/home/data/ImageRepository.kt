package com.example.clean.entry.home.data

import com.example.clean.entry.home.data.models.PexelsPhotosResponse

interface ImageRepository {
    suspend fun getCuratedPhotos(): PexelsPhotosResponse
}