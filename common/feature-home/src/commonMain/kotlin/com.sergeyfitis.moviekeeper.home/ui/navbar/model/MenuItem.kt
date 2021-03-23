package com.sergeyfitis.moviekeeper.home.ui.navbar.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.sergeyfitis.moviekeeper.home.ca.state.navbar.NavBarTab

internal data class MenuItem(
    val title: String = "",
    val icon: ImageVector? = null,
    val navBarTab: NavBarTab
)