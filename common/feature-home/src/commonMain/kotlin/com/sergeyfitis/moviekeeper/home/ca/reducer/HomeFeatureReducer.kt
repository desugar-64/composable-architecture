package com.sergeyfitis.moviekeeper.home.ca.reducer

import com.sergeyfitis.moviekeeper.home.ca.action.HomeFeatureAction
import com.sergeyfitis.moviekeeper.home.ca.action.navBar.NavBarAction
import com.sergeyfitis.moviekeeper.home.ca.action.navBarAction
import com.sergeyfitis.moviekeeper.home.ca.state.HomeFeatureState
import com.sergeyfitis.moviekeeper.home.ca.state.navBarState
import com.sergeyfitis.moviekeeper.home.ca.state.navbar.NavBarHeader
import com.sergeyfitis.moviekeeper.home.ca.state.navbar.NavBarState
import com.syaremych.composable_architecture.prelude.identity
import com.syaremych.composable_architecture.store.Reducer
import com.syaremych.composable_architecture.store.pullback
import com.syaremych.composable_architecture.store.reduced

internal val navigationBarReducer = Reducer<NavBarState, NavBarAction, Unit> { state, action, _ ->
    val newState = when(action) {
        is NavBarAction.TabSelected -> {
            state.copy(activeNavBarItem = (action.parentHeader to action.navBarTab))
        }
    }
    reduced(newState)
}

val homeFeatureReducer = navigationBarReducer.pullback(
    value = HomeFeatureState.navBarState,
    action = HomeFeatureAction.navBarAction,
    environment = ::identity
)