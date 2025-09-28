package com.example.clean.entry.details.domain.usecase

import com.example.clean.entry.shared.data.ImageRepository
import com.example.clean.entry.shared.domain.model.Image

class GetImageDetailsUseCase(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(imageId: Long): Result<Image> {
        return imageRepository.getImageById(imageId)
    }
}
