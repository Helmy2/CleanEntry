package com.example.clean.entry.feed.data

import com.example.clean.entry.feed.data.models.PexelsPhotosResponse

interface ImageRepository {
    suspend fun getCuratedPhotos(): PexelsPhotosResponse
}