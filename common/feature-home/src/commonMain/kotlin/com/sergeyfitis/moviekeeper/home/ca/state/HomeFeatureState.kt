package com.sergeyfitis.moviekeeper.home.ca.state

enum class NavBarHeader {
    MOVIES, TV_SHOWS, PEOPLE, MY_FAVORITES
}

internal enum class NavBarTab {
    POPULAR, NOW_PLAYING, UPCOMING, TOP_RATED, AIRING_TODAY, ON_TV, POPULAR_PEOPLE
}

internal data class NavBarItem(
    val header: NavBarHeader,
    val tabs: List<NavBarTab>
)

internal typealias ActiveNavBarItem = Pair<NavBarHeader, NavBarTab>

internal data class NavBarState(
    val items: List<NavBarItem>,
    val activeNavBarItem: ActiveNavBarItem?
) {
    companion object {
        fun init() = NavBarState(
            items = listOf(
                NavBarItem(
                    NavBarHeader.MOVIES,
                    listOf(
                        NavBarTab.TOP_RATED,
                        NavBarTab.NOW_PLAYING,
                        NavBarTab.POPULAR,
                        NavBarTab.UPCOMING
                    )
                ),
                NavBarItem(
                    NavBarHeader.TV_SHOWS,
                    listOf(
                        NavBarTab.AIRING_TODAY,
                        NavBarTab.POPULAR,
                        NavBarTab.ON_TV,
                        NavBarTab.TOP_RATED
                    )
                )
            ),
            activeNavBarItem = null
        )
    }
}

data class HomeFeatureState internal constructor(
    internal val navBarState: NavBarState,
) {
    companion object {
        fun init() = HomeFeatureState(
            navBarState = NavBarState.init()
        )
    }
}