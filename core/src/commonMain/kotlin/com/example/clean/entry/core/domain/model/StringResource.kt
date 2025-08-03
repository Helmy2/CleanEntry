package com.example.clean.entry.core.domain.model

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource


sealed interface StringResource {
    class FromId(val id: org.jetbrains.compose.resources.StringResource, vararg val args: Any) : StringResource
    data class FromString(val value: String) : StringResource
}


@Composable
fun stringResource(resource: StringResource): String {
    return when (resource) {
        is StringResource.FromId -> stringResource(resource.id, *resource.args)
        is StringResource.FromString -> resource.value
    }
}

