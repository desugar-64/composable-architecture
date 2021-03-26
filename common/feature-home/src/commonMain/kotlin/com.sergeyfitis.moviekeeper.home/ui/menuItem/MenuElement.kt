package com.sergeyfitis.moviekeeper.home.ui.menuItem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.sergeyfitis.moviekeeper.ext.applyIf
import com.sergeyfitis.moviekeeper.home.ui.navbar.model.MenuItem

@Composable
internal fun MenuElement(
    modifier: Modifier = Modifier,
    menuItem: MenuItem.Element,
    isSelected: Boolean,
    padding: PaddingValues = PaddingValues(horizontal = 32.dp, vertical = 8.dp),
    shape: Shape = RoundedCornerShape(4.dp),
    onItemClicked: (MenuItem.Element) -> Unit
) {
    Text(
        text = menuItem.title,
        modifier = modifier
            .applyIf(isSelected) {
                background(
                    color = Color.LightGray.copy(alpha = .3f),
                    shape = shape
                )
            }
            .clickable(role = Role.Tab) { onItemClicked.invoke(menuItem) }
            .padding(padding)
    )
}