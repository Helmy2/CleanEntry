package com.example.clean.entry.core.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource


sealed interface StringResource {
    class FromId(@param:StringRes val id: Int, vararg val args: Any) : StringResource
    data class FromString(val value: String) : StringResource
}


@Composable
fun stringResource(resource: StringResource): String {
    return when (resource) {
        is StringResource.FromId -> stringResource(resource.id, *resource.args)
        is StringResource.FromString -> resource.value
    }
}

