package com.sergeyfitis.moviekeeper.home.ui.navbar.model

import com.sergeyfitis.moviekeeper.home.ca.state.NavBarTab

internal data class MenuItem(
    val title: String = "",
    val icon: Int = 0,
    val navBarTab: NavBarTab
)