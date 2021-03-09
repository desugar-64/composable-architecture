package com.sergeyfitis.moviekeeper.home

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val W510 = 510.dp

@Composable
fun Home() {
    MaterialTheme {
        BoxWithConstraints {
            when {
                maxWidth >= W510 -> Text("Tablet|PC")
                else -> Text("Mobile")
            }
        }
    }
}