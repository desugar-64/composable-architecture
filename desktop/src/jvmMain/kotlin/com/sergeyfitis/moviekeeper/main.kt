package com.sergeyfitis.moviekeeper

import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.sergeyfitis.moviekeeper.ui.MoviePoster
import com.sergeyfitis.moviekeeper.home.Home
import com.sergeyfitis.moviekeeper.home.ca.state.HomeFeatureState
import com.sergeyfitis.moviekeeper.home.ui.navbar.HomeRootView


fun main() = Window(
    title = "Movie Keeper Compose Desktop",
    size = IntSize(640, 480),
    icon = null,
) {
    HomeRootView(homeFeatureState = HomeFeatureState.init())
}