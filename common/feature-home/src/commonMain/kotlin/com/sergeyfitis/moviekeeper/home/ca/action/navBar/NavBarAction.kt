package com.sergeyfitis.moviekeeper.home.ca.action.navBar

import com.sergeyfitis.moviekeeper.home.ca.state.navbar.NavBarHeader
import com.sergeyfitis.moviekeeper.home.ca.state.navbar.NavBarTab

internal sealed class NavBarAction {
    data class TabSelected(
        val parentHeader: NavBarHeader,
        val navBarTab: NavBarTab
    ) : NavBarAction()
}