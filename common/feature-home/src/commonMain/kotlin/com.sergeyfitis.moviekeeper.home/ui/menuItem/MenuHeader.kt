package com.sergeyfitis.moviekeeper.home.ui.menuItem

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.sergeyfitis.moviekeeper.home.ui.navbar.model.MenuItem

@Composable
internal fun MenuHeader(
    modifier: Modifier = Modifier,
    menuHeader: MenuItem.Header
) {
    Text(
        modifier = modifier,
        text = menuHeader.title,
        style = MaterialTheme.typography.caption.copy(color = Color.Red.copy(alpha = .7f, blue = 0.1f), fontWeight = FontWeight.ExtraBold)
    )
}