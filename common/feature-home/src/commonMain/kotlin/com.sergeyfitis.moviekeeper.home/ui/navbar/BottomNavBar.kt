package com.sergeyfitis.moviekeeper.home.ui.navbar

import androidx.compose.foundation.background
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.LocalWindowInfo

@Composable
internal fun BottomNavBar(modifier: Modifier = Modifier) {
    Text(text = "BottomNavBar", modifier = modifier.background(color = Color.Green))

}