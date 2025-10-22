package com.example.clean.entry.details.domain.usecase

import com.example.clean.entry.shared.data.ImageRepository
import com.example.clean.entry.shared.domain.model.Image

class GetSimilarImagesUseCase(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(query: String, perPage: Int = 20): Result<List<Image>> {
        if (query.isBlank()) {
            return Result.success(emptyList())
        }
        return imageRepository.searchPhotosByQuery(query = query, page = 1, perPage = perPage)
    }
}
