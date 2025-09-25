package com.example.clean.entry.feed.domain.usecase

import com.example.clean.entry.core.util.runCatchingOnIO
import com.example.clean.entry.feed.data.ImageRepository
import com.example.clean.entry.feed.data.mapper.toDomainModel
import com.example.clean.entry.feed.domain.model.Image

class GetImagesUseCase(
    private val repository: ImageRepository
) {
    suspend operator fun invoke(): Result<List<Image>> {
        return runCatchingOnIO {
            repository.getCuratedPhotos().photos.map { it.toDomainModel() }
        }
    }
}