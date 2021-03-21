package com.sergeyfitis.moviekeeper.home.ui.menuItem

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sergeyfitis.moviekeeper.home.ca.state.NavBarTab
import com.sergeyfitis.moviekeeper.home.ui.navbar.model.MenuItem

@Composable
internal fun MenuElement(
    modifier: Modifier = Modifier,
    menuItem: MenuItem,
    onItemClicked: (NavBarTab) -> Unit
) {
    Text(
        text = menuItem.title,
        modifier = modifier.clickable { onItemClicked.invoke(menuItem.navBarTab) }

    )
}