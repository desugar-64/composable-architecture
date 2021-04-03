package com.sergeyfitis.moviekeeper.home

import androidx.compose.runtime.Composable
import com.sergeyfitis.moviekeeper.home.ca.action.HomeFeatureAction
import com.sergeyfitis.moviekeeper.home.ca.state.HomeFeatureState
import com.sergeyfitis.moviekeeper.home.ui.navbar.HomeRootView
import com.syaremych.composable_architecture.store.Store


@Composable
fun HomeScreen(homeFeatureStore: Store<HomeFeatureState, HomeFeatureAction>) {
    HomeRootView(homeFeatureStore)
}