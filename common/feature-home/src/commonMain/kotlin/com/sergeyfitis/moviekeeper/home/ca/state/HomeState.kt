package com.sergeyfitis.moviekeeper.home.ca.state

import androidx.compose.runtime.Immutable



@Immutable
internal data class HomeState(
    val navBarItems: List<NavBarItem>,
    val activeNavBarItem: ActiveNavBarItem
)