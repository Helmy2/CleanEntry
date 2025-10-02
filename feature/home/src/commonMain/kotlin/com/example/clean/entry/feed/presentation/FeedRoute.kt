package com.example.clean.entry.feed.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.example.clean.entry.core.components.ErrorScreen
import com.example.clean.entry.core.components.shimmer
import com.example.clean.entry.shared.domain.model.Image
import org.jetbrains.compose.resources.StringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.random.Random

@Composable
fun FeedRoute(
    viewModel: FeedViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    FeedScreen(
        isLoading = state.isLoading,
        images = state.images,
        error = state.error,
        handleEvent = viewModel::handleEvent,
    )
}

@Composable
fun FeedScreen(
    isLoading: Boolean,
    images: List<Image>,
    error: StringResource?,
    handleEvent: (FeedReducer.Event) -> Unit,
) {
    Box {
        when {
            error != null -> {
                ErrorScreen(
                    message = error,
                    onRetry = {
                        handleEvent(FeedReducer.Event.LoadImages)
                    }
                )
            }

            else -> {
                Scaffold {
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = it.calculateTopPadding()),
                        contentAlignment = Alignment.Center
                    ) {
                        val columns = if (maxWidth > 600.dp) {
                            StaggeredGridCells.Fixed(3)
                        } else {
                            StaggeredGridCells.Fixed(2)
                        }

                        LazyVerticalStaggeredGrid(
                            columns = columns,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalItemSpacing = 16.dp
                        ) {
                            if (isLoading) {
                                items(20) {
                                    Card {
                                        Box(
                                            modifier = Modifier.fillMaxWidth()
                                                .height(Random.nextInt(100, 300).dp)
                                                .animateContentSize().shimmer()
                                        )
                                    }
                                }
                            } else {
                                items(images) { image ->
                                    ImageCard(
                                        image = image,
                                        onImageClick = {
                                            handleEvent(FeedReducer.Event.ImageClicked(image.id))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImageCard(
    image: Image,
    modifier: Modifier = Modifier,
    onImageClick: () -> Unit
) {
    Card(
        modifier = modifier
            .animateContentSize()
            .clickable(onClick = onImageClick),
        colors = CardDefaults.cardColors(
            containerColor = image.avgColor ?: Color.Unspecified,
        )
    ) {
        SubcomposeAsyncImage(
            model = image.medium,
            contentDescription = image.photographer,
            modifier = Modifier.fillMaxWidth().aspectRatio(image.aspectRatio),
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .aspectRatio(image.aspectRatio)
                        .shimmer()
                )
            },
        )
        Text(
            text = "Photo by ${image.photographer}",
            modifier = Modifier.padding(8.dp),
            maxLines = 2,
            minLines = 2
        )
    }
}