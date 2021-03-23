package com.sergeyfitis.moviekeeper.home.ca.viewState

import androidx.compose.runtime.Immutable
import com.sergeyfitis.moviekeeper.home.ui.navbar.model.MenuItem

@Immutable
internal class State(
    val selectedMenuItem: MenuItem,
    val menuItems: List<MenuItem>
)