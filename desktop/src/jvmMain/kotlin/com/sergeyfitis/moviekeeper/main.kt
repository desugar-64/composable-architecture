package com.sergeyfitis.moviekeeper

import androidx.compose.desktop.Window
import androidx.compose.ui.unit.IntSize
import com.sergeyfitis.moviekeeper.home.HomeScreen
import com.sergeyfitis.moviekeeper.home.ca.reducer.homeFeatureReducer
import com.sergeyfitis.moviekeeper.home.ca.state.HomeFeatureState
import com.syaremych.composable_architecture.store.Store


fun main() = Window(
    title = "Movie Keeper Compose Desktop",
    size = IntSize(768, 640),
    icon = null,
) {
    val homeFeatureStore = Store.init(
        initialState = HomeFeatureState.init(),
        reducer = homeFeatureReducer,
        environment = Unit
    )
    HomeScreen(homeFeatureStore)
}