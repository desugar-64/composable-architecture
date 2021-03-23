package com.sergeyfitis.moviekeeper.home.ca.state

import com.sergeyfitis.moviekeeper.home.ca.state.navbar.NavBarState
import com.syaremych.composable_architecture.prelude.types.Lens

data class HomeFeatureState internal constructor(
    internal val navBarState: NavBarState,
) {
    companion object {
        fun init() = HomeFeatureState(
            navBarState = NavBarState.init()
        )
    }
}

internal val HomeFeatureState.Companion.navBarState
    get() = Lens<HomeFeatureState, NavBarState>(
        get = { featureState -> featureState.navBarState },
        set = { featureState, viewState -> featureState.copy(navBarState = viewState) }
    )