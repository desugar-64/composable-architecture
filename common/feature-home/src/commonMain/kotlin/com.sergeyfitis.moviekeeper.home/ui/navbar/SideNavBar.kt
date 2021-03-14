package com.sergeyfitis.moviekeeper.home.ui.navbar

import androidx.compose.foundation.background
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
internal fun SideNavBar(modifier: Modifier = Modifier) {
    Text(text = "SideNavBar", modifier = modifier.background(color = Color.Cyan))
}