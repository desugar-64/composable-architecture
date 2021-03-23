package com.sergeyfitis.moviekeeper.home.ui.navbar

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.sergeyfitis.moviekeeper.home.ca.action.navBar.NavBarAction
import com.sergeyfitis.moviekeeper.home.ca.state.navbar.NavBarState
import com.sergeyfitis.moviekeeper.home.ca.viewState.NavBarViewState
import com.sergeyfitis.moviekeeper.home.ca.viewState.init
import com.sergeyfitis.moviekeeper.home.ui.menuItem.MenuElement
import com.sergeyfitis.moviekeeper.home.ui.navbar.model.MenuItem
import com.syaremych.composable_architecture.prelude.identity
import com.syaremych.composable_architecture.store.Store
import com.syaremych.composable_architecture.store.ViewStore
import com.syaremych.composable_architecture.store.view

@Composable
internal fun SideNavBar(
    modifier: Modifier = Modifier,
    navBarStore: Store<NavBarState, NavBarAction>,
) {
    val navBarViewStore: ViewStore<NavBarViewState, NavBarAction> = remember(navBarStore) {
        navBarStore.scope(toLocalValue = NavBarViewState::init, toGlobalAction = ::identity).view
    }

    val state by navBarViewStore.collectAsState(initial = NavBarViewState.init(navBarStore.state))

    Column(modifier = modifier) {
        state.menuItems.forEach { item ->
            when (item) {
                is MenuItem.Header -> TODO()
                is MenuItem.Element -> MenuElement(menuItem = item) { clickedTab ->
                   navBarViewStore.send(NavBarAction.NavigateTo(clickedTab))
                }
            }
        }
    }
}