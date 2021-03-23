package com.sergeyfitis.moviekeeper.home.ca.action.navBar

import com.sergeyfitis.moviekeeper.home.ca.state.navbar.NavBarTab

internal sealed class NavBarAction {
    data class NavigateTo(val navBarTab: NavBarTab) : NavBarAction()
}