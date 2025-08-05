package com.example.clean.entry.feature_auth.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.clean.entry.core.components.shimmer
import com.example.clean.entry.core.design_system.spacing
import com.example.clean.entry.feature_auth.domain.model.Country
import com.example.clean.entry.R

@Composable
fun CountryRow(
    country: Country,
    isSelected: Boolean,
    onClick: (Country) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = { onClick(country) })
                .padding(MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = country.flagEmoji,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
            Text(
                text = country.code,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Text(
                text = country.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.weight(1f))
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = stringResource(R.string.selected),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        HorizontalDivider()
    }
}

@Composable
fun CountryRowShimmer(modifier: Modifier = Modifier) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(MaterialTheme.typography.titleLarge.lineHeight.value.dp)
                    .clip(RoundedCornerShape(10))
                    .shimmer()
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(MaterialTheme.typography.bodyLarge.lineHeight.value.dp)
                    .clip(RoundedCornerShape(10))
                    .shimmer()
            )

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.typography.bodyLarge.lineHeight.value.dp)
                    .clip(RoundedCornerShape(10))
                    .shimmer()
            )
        }
        HorizontalDivider()
    }
}