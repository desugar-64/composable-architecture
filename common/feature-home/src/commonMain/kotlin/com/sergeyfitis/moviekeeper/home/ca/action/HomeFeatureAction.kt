package com.sergeyfitis.moviekeeper.home.ca.action

import com.sergeyfitis.moviekeeper.home.ca.action.navBar.NavBarAction
import com.syaremych.composable_architecture.prelude.types.Prism

sealed class HomeFeatureAction {
    internal data class NavBar(val navbarAction: NavBarAction) : HomeFeatureAction()

    companion object
}

internal val HomeFeatureAction.Companion.navBarAction: Prism<HomeFeatureAction, NavBarAction>
    get() = Prism(
        get = { featureAction ->
            when (featureAction) {
                is HomeFeatureAction.NavBar -> featureAction.navbarAction
            }
        },
        reverseGet = { viewAction ->
            HomeFeatureAction.NavBar(viewAction)
        }
    )
