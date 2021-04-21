package com.sergeyfitis.moviekeeper.home.ui.navbar

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sergeyfitis.moviekeeper.home.ca.action.navBar.NavBarAction
import com.sergeyfitis.moviekeeper.home.ca.state.navbar.NavBarState
import com.sergeyfitis.moviekeeper.home.ca.viewState.NavBarViewState
import com.sergeyfitis.moviekeeper.home.ca.viewState.init
import com.sergeyfitis.moviekeeper.home.ui.menuItem.MenuElement
import com.sergeyfitis.moviekeeper.home.ui.menuItem.MenuHeader
import com.sergeyfitis.moviekeeper.home.ui.navbar.model.MenuItem
import com.syaremych.composable_architecture.compose_ui.collectAsState
import com.syaremych.composable_architecture.compose_ui.rememberViewStore
import com.syaremych.composable_architecture.prelude.identity
import com.syaremych.composable_architecture.store.Store

@Composable
internal fun SideNavBar(
    modifier: Modifier = Modifier,
    navBarStore: Store<NavBarState, NavBarAction>,
) {
    val fillWidth = Modifier.fillMaxWidth()

    val navBarViewStore = rememberViewStore(navBarStore, toViewState = NavBarViewState::init, toAction = ::identity)
    val state by navBarViewStore.collectAsState()

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

}