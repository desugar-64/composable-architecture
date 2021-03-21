package com.sergeyfitis.moviekeeper.home.ui.navbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.sergeyfitis.moviekeeper.ext.applyIf
import com.sergeyfitis.moviekeeper.home.ca.state.HomeFeatureState

private enum class HomeScaffoldLayout { SIDE_BAR, BOTTOM_BAR, CONTENT }

@Composable
fun HomeRootView(homeFeatureState: HomeFeatureState) = HomeScaffold(
    sideNavigationBar = {
        var maxMenuItemWidthPx: Int by remember { mutableStateOf(0) }
        SideNavBar(Modifier.fillMaxHeight().wrapContentWidth()) {
            homeFeatureState.navBarState.items.forEach { navBarItem ->
                Text(
                    text = navBarItem.header.name,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .onSizeChanged {
                            if (it.width > maxMenuItemWidthPx) {
                                maxMenuItemWidthPx = it.width
                            }
                        }
                        .applyIf(maxMenuItemWidthPx > 0) {
                            requiredWidth(width = with(LocalDensity.current) { maxMenuItemWidthPx.toDp() })
                        }
                        .background(
                            shape = RoundedCornerShape(4.dp),
                            color = Color.Green
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                navBarItem.tabs.forEach { tab ->
                    Text(
                        text = tab.name,
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                            .onSizeChanged {
                                if (it.width > maxMenuItemWidthPx) {
                                    maxMenuItemWidthPx = it.width
                                }
                            }
                            .applyIf(maxMenuItemWidthPx > 0) {
                                requiredWidth(width = with(LocalDensity.current) { maxMenuItemWidthPx.toDp() })
                            }
                            .background(
                                shape = RoundedCornerShape(4.dp),
                                color = Color.LightGray
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    },
    bottomNavigationBar = {
        BottomNavBar(Modifier.fillMaxWidth().requiredHeight(48.dp))
    }
) {
    Box(
        Modifier.fillMaxSize()
            .background(color = Color.Gray)
            .padding(16.dp)
    ) {
        Text("Content")
    }
}

@Composable
private fun HomeScaffold(
    modifier: Modifier = Modifier,
    sideNavigationBar: @Composable () -> Unit = {},
    bottomNavigationBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) = BoxWithConstraints(modifier = modifier) {

    val isTablet = maxWidth > 510.dp

    val scaffoldContent: @Composable () -> Unit = {
        if (isTablet) {
            Box(Modifier.layoutId(HomeScaffoldLayout.SIDE_BAR)) { sideNavigationBar() }
        } else {
            Box(Modifier.layoutId(HomeScaffoldLayout.BOTTOM_BAR)) { bottomNavigationBar() }
        }
        Box(Modifier.layoutId(HomeScaffoldLayout.CONTENT)) { content() }
    }

    Layout(modifier = modifier, content = scaffoldContent) { measurables, constraints ->
        var contentHeight: Int = constraints.maxHeight
        var contentWidth: Int = constraints.maxWidth

        val sideBarPlaceable =
            measurables
                .find { it.layoutId == HomeScaffoldLayout.SIDE_BAR }
                ?.measure(constraints)
                ?.also { sideBarPlaceable -> contentWidth -= sideBarPlaceable.width }

        val bottomBarPlaceable =
            measurables
                .find { it.layoutId == HomeScaffoldLayout.BOTTOM_BAR }
                ?.measure(constraints)
                ?.also { bottomBarPlaceable -> contentHeight -= bottomBarPlaceable.height }

        val contentPlaceable =
            measurables
                .first { it.layoutId == HomeScaffoldLayout.CONTENT }
                .measure(constraints.copy(maxWidth = contentWidth, maxHeight = contentHeight))


        layout(constraints.maxWidth, constraints.maxHeight) {

            sideBarPlaceable?.placeRelative(x = 0, y = 0)

            bottomBarPlaceable?.let { placeable ->
                placeable.placeRelative(x = 0, y = constraints.maxHeight - placeable.height)
            }

            val contentX = sideBarPlaceable?.width ?: 0
            val contentY = 0

            contentPlaceable.placeRelative(x = contentX, y = contentY)
        }
    }
}