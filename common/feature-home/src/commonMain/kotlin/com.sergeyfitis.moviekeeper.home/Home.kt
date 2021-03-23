package com.sergeyfitis.moviekeeper.home

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.sergeyfitis.moviekeeper.home.ca.action.HomeFeatureAction
import com.sergeyfitis.moviekeeper.home.ca.state.HomeFeatureState
import com.sergeyfitis.moviekeeper.home.ca.state.navBarState
import com.sergeyfitis.moviekeeper.home.ca.viewAction.init
import com.sergeyfitis.moviekeeper.home.ui.navbar.HomeRootView
import com.syaremych.composable_architecture.store.Store
import com.syaremych.composable_architecture.store.view

private val W510 = 510.dp

@Composable
fun HomeScreen(homeFeatureStore: Store<HomeFeatureState, HomeFeatureAction>) {
    MaterialTheme {
        HomeRootView(homeFeatureStore)
    }
}