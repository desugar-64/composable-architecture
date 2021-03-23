package com.sergeyfitis.moviekeeper.home.ca.viewAction

import com.sergeyfitis.moviekeeper.home.ca.action.HomeFeatureAction
import com.sergeyfitis.moviekeeper.home.ca.action.navBar.NavBarAction
import com.sergeyfitis.moviekeeper.home.ui.navbar.model.MenuItem

internal sealed class Action {
    data class TapMenuItem(val menuItem: MenuItem.Element) : Action()

    companion object
}

internal fun HomeFeatureAction.Companion.init(action: Action): HomeFeatureAction {
    return when (action) {
        is Action.TapMenuItem -> HomeFeatureAction.NavBar(NavBarAction.NavigateTo(action.menuItem.navBarTab))
    }
}