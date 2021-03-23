package com.sergeyfitis.moviekeeper.home.ca.viewState

import androidx.compose.runtime.Immutable
import com.sergeyfitis.moviekeeper.home.ca.state.navbar.NavBarItem
import com.sergeyfitis.moviekeeper.home.ca.state.navbar.NavBarState
import com.sergeyfitis.moviekeeper.home.ui.navbar.model.MenuItem
import com.sergeyfitis.moviekeeper.home.ui.navbar.model.toMenuItems

@Immutable
internal class NavBarViewState(
    val selectedMenuItem: MenuItem,
    val menuItems: List<MenuItem>
) { companion object }

internal fun NavBarViewState.Companion.init(state: NavBarState): NavBarViewState {
    var activeItem: MenuItem? = null
    val items = state.items.flatMap(NavBarItem::toMenuItems)
    if (state.activeNavBarItem != null) {
        val (_, tab) = state.activeNavBarItem
        activeItem = items.find {
            when(it) {
                is MenuItem.Element -> it.navBarTab == tab
                else -> false
            }
        }
    }
    return NavBarViewState(
        selectedMenuItem = activeItem ?: items[1],
        menuItems = items
    )
}
