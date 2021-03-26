package com.sergeyfitis.moviekeeper.home

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.sergeyfitis.moviekeeper.home.ca.action.HomeFeatureAction
import com.sergeyfitis.moviekeeper.home.ca.state.HomeFeatureState
import com.sergeyfitis.moviekeeper.home.ui.navbar.HomeRootView
import com.syaremych.composable_architecture.store.Store

private val W510 = 510.dp

@Composable
fun HomeScreen(homeFeatureStore: Store<HomeFeatureState, HomeFeatureAction>) {
    MaterialTheme(colors = darkColors()) {
        HomeRootView(homeFeatureStore)
    }
}