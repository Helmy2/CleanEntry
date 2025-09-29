package com.example.clean.entry.shared.di

import com.example.clean.entry.details.domain.usecase.GetImageDetailsUseCase
import com.example.clean.entry.details.domain.usecase.GetSimilarImagesUseCase
import com.example.clean.entry.details.presentation.DetailsViewModel
import com.example.clean.entry.feed.domain.usecase.GetImagesUseCase
import com.example.clean.entry.feed.presentation.FeedViewModel
import com.example.clean.entry.shared.data.ImageRepository
import com.example.clean.entry.shared.data.ImageRepositoryImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeModule = module {
    singleOf(::ImageRepositoryImpl).bind<ImageRepository>()
    singleOf(::GetImagesUseCase)
    singleOf(::FeedViewModel)

    factoryOf(::GetImageDetailsUseCase)
    factoryOf(::GetSimilarImagesUseCase)
    viewModelOf(::DetailsViewModel)
}