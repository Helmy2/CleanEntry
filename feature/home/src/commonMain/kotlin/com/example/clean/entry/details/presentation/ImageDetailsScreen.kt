package com.example.clean.entry.details.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.clean.entry.core.components.TopBarWithBackNavigation
import com.example.clean.entry.shared.domain.model.Image

@Composable
fun ImageDetailsScreen(
    viewModel: ImageDetailsViewModel
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopBarWithBackNavigation(
                title = "Image Details",
                onBackClick = {
                    viewModel.handleEvent(ImageDetailsReducer.Event.BackButtonClicked)
                },
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        when {
            state.isLoading -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Error: ${state.error}", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.handleEvent(ImageDetailsReducer.Event.RetryLoadDetails) }) {
                        Text("Retry")
                    }
                }
            }

            state.currentImage != null -> {
                ImageDetailsContent(
                    image = state.currentImage!!,
                    similarImages = state.similarImages,
                    isLoadingSimilar = state.isLoadingSimilar,
                    onSimilarImageClick = { similarImageId ->
                        viewModel.handleEvent(
                            ImageDetailsReducer.Event.SimilarImageClicked(
                                similarImageId
                            )
                        )
                    },
                    modifier = Modifier.verticalScroll(
                        rememberScrollState()
                    )
                )
            }

            else -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        "No image details to display.",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Button(onClick = {
                        viewModel.handleEvent(
                            ImageDetailsReducer.Event.ScreenOpened(
                                state.imageId
                            )
                        )
                    }) {
                        Text("Load Details")
                    }
                }
            }
        }
    }
}

@Composable
fun ImageDetailsContent(
    image: Image,
    similarImages: List<Image>,
    isLoadingSimilar: Boolean,
    onSimilarImageClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = image.large,
            contentDescription = image.alt,
            modifier = Modifier.aspectRatio(image.aspectRatio),
            contentScale = ContentScale.Crop
        )

        image.photographer.takeIf { it.isNotEmpty() }.let {
            Text(
                "Photographer: $it",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        image.alt.takeIf { it.isNotEmpty() }.let {
            Text(
                "Description: $it",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Text(
            "Similar Images",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        if (isLoadingSimilar) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (similarImages.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            ) {
                items(similarImages, key = { it.id }) { similarImage ->
                    Card {
                        AsyncImage(
                            model = similarImage.medium,
                            contentDescription = "Similar image ${similarImage.id}",
                            modifier = Modifier
                                .size(200.dp)
                                .clickable { onSimilarImageClick(similarImage.id) },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        } else {
            Text(
                "No similar images found.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}