package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview

@Composable
internal fun MovieTab(
    modifier: Modifier = Modifier,
    tab: Tab,
    isActive: Boolean,
    onClick: (Tab) -> Unit
) {

    Text(
        modifier = modifier
            .clickable(onClick = { onClick(tab) })
            .padding(8.dp),
        text = stringResource(id = tab.title),
        fontSize = if (isActive) 16.sp else 14.sp,
        fontFamily = if (isActive) FontFamily.SansSerif else FontFamily.Default,
        color = if (isActive) Color.DarkGray else Color.Gray
    )
}

@Preview(name = "Tab inactive", backgroundColor = 0xffffffL, showBackground = true)
@Composable
private fun tabPreviewInactive() {
    MovieTab(tab = Tab.Popular, isActive = false, onClick = {})
}

@Preview(name = "Tab active", backgroundColor = 0xffffffL, showBackground = true)
@Composable
private fun tabPreviewActive() {
    MovieTab(tab = Tab.Popular, isActive = true, onClick = {})
}