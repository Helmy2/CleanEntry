package com.example.clean.entry.home.domain.usecase

import com.example.clean.entry.core.util.runCatchingOnIO
import com.example.clean.entry.home.data.ImageRepository
import com.example.clean.entry.home.data.mapper.toDomainModel
import com.example.clean.entry.home.domain.model.Image

class GetImagesUseCase(
    private val repository: ImageRepository
) {
    suspend operator fun invoke(): Result<List<Image>> {
        return runCatchingOnIO {
            repository.getCuratedPhotos().photos.map { it.toDomainModel() }
        }
    }
}