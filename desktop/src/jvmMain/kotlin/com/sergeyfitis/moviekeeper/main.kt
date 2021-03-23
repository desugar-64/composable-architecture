package com.sergeyfitis.moviekeeper

import androidx.compose.desktop.Window
import androidx.compose.ui.unit.IntSize
import com.sergeyfitis.moviekeeper.home.HomeScreen
import com.sergeyfitis.moviekeeper.home.ca.action.HomeFeatureAction
import com.sergeyfitis.moviekeeper.home.ca.state.HomeFeatureState
import com.syaremych.composable_architecture.store.Store


fun main() = Window(
    title = "Movie Keeper Compose Desktop",
    size = IntSize(640, 480),
    icon = null,
) {
    val homeFeatoreStore = Store.init<HomeFeatureState, HomeFeatureAction, Unit>(
        initialState = HomeFeatureState.init(),
        reducer = TODO(),
        environment = Unit
    )
    HomeScreen(homeFeatoreStore)
}