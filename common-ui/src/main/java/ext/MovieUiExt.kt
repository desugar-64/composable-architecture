package ext

import androidx.compose.ui.Modifier

fun Modifier.applyIf(
    condition: Boolean,
    modifier: Modifier.() -> Modifier
) = if (condition) modifier() else this