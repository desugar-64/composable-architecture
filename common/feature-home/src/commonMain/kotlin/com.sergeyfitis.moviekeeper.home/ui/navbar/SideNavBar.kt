package com.sergeyfitis.moviekeeper.home.ui.navbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sergeyfitis.moviekeeper.home.ca.action.navBar.NavBarAction
import com.sergeyfitis.moviekeeper.home.ca.state.navbar.NavBarState
import com.sergeyfitis.moviekeeper.home.ca.viewState.NavBarViewState
import com.sergeyfitis.moviekeeper.home.ca.viewState.init
import com.sergeyfitis.moviekeeper.home.ui.menuItem.MenuElement
import com.sergeyfitis.moviekeeper.home.ui.menuItem.MenuHeader
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
    val fillWidth = Modifier.fillMaxWidth()

    val navBarViewStore: ViewStore<NavBarViewState, NavBarAction> = remember(navBarStore) {
        navBarStore.scope(toLocalValue = NavBarViewState::init, toGlobalAction = ::identity).view
    }

    val state by navBarViewStore.collectAsState(initial = NavBarViewState.init(navBarStore.state))

    Surface(modifier = modifier) {
        Column {
            state.menuItems.forEachIndexed { index, item ->
                if (index == 0) {
                    Spacer(Modifier.height(16.dp))
                }
                when (item) {
                    is MenuItem.Header -> MenuHeader(
                        modifier = fillWidth.padding(horizontal = 32.dp, vertical = 8.dp),
                        menuHeader = item
                    )
                    is MenuItem.Element -> MenuElement(
                        modifier = fillWidth,
                        isSelected = state.selectedMenuItem == item,
                        menuItem = item
                    ) { clickedTab ->
                        navBarViewStore.send(
                            NavBarAction.TabSelected(
                                parentHeader = clickedTab.parentHeader,
                                navBarTab = clickedTab.navBarTab
                            )
                        )
                    }
                }
            }
        }
    }

    DisposableEffect(navBarViewStore) {
        onDispose(navBarViewStore::dispose)
    }
}