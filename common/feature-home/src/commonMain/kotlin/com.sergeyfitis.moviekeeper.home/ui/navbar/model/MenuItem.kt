package com.sergeyfitis.moviekeeper.home.ui.navbar.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material.icons.filled.OnlinePrediction
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Upcoming
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import com.sergeyfitis.moviekeeper.home.ca.state.navbar.NavBarHeader
import com.sergeyfitis.moviekeeper.home.ca.state.navbar.NavBarItem
import com.sergeyfitis.moviekeeper.home.ca.state.navbar.NavBarTab

@Stable
internal sealed class MenuItem {
    data class Header(val title: String, val header: NavBarHeader) : MenuItem()
    data class Element(
        val title: String,
        val icon: ImageVector?,
        val navBarTab: NavBarTab,
        val parentHeader: NavBarHeader
    ) : MenuItem()
}

internal fun NavBarItem.toMenuItems(): List<MenuItem> {
    val items = mutableListOf<MenuItem>()

    val header = MenuItem.Header(title = headerStringOf(header), header = header)
    items += header

    items += tabs.map { tab ->
        MenuItem.Element(
            title = tabStringOf(tab),
            icon = tabIconOf(tab),
            navBarTab = tab,
            parentHeader = this.header
        )
    }

    return items
}

// TODO: Make platform implementations
private fun headerStringOf(header: NavBarHeader): String {
    return when (header) {
        NavBarHeader.MOVIES -> "Movies"
        NavBarHeader.TV_SHOWS -> "TV Shows"
        NavBarHeader.PEOPLE -> "People"
        NavBarHeader.MY_FAVORITES -> "Favorite"
    }
}

// TODO: Make platform implementations
private fun tabStringOf(tab: NavBarTab): String {
    return when (tab) {
        NavBarTab.POPULAR -> "Popular"
        NavBarTab.NOW_PLAYING -> "Now Playing"
        NavBarTab.UPCOMING -> "Upcoming"
        NavBarTab.TOP_RATED -> "Top Rated"
        NavBarTab.AIRING_TODAY -> "Airing Today"
        NavBarTab.ON_TV -> "On TV"
        NavBarTab.POPULAR_PEOPLE -> "Popular People"
    }
}

private fun tabIconOf(tab: NavBarTab): ImageVector {
    return when (tab) {
        NavBarTab.POPULAR -> Icons.Filled.PermMedia
        NavBarTab.NOW_PLAYING -> Icons.Filled.OnlinePrediction
        NavBarTab.UPCOMING -> Icons.Filled.Upcoming
        NavBarTab.TOP_RATED -> Icons.Filled.Stars
        NavBarTab.AIRING_TODAY -> Icons.Filled.Today
        NavBarTab.ON_TV -> Icons.Filled.Tv
        NavBarTab.POPULAR_PEOPLE -> Icons.Filled.EmojiPeople
    }
}