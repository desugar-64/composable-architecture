package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui

import androidx.annotation.StringRes
import com.sergeyfitis.moviekeeper.feature_movies_list.R

internal enum class Tab(
    @StringRes val title: Int,
) {
    Recomended(R.string.movie_tab_recomended),
    Popular(R.string.movie_tab_popular),
    Recent(R.string.movie_tab_recent)
}