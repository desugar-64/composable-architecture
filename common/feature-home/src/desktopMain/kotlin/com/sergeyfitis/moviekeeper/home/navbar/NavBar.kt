package com.sergeyfitis.moviekeeper.home.navbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
actual fun HomeNavBar() {
    Row {
        Text(text = "Now Playing")
        Text(text = "Upcoming")
        Text(text = "Top Rated")
    }
}