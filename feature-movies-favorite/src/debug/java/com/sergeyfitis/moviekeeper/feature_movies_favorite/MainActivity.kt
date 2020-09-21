package com.sergeyfitis.moviekeeper.feature_movies_favorite

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.sergeyfitis.moviekeeper.data.models.Movie
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.reducer.favoriteMoviesFeatureReducer
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.state.FavoriteFeatureState
import com.syaremych.composable_architecture.store.Store

private val movies = listOf(
    Movie(0, "Rogue", "/uOw5JD8IlD546feZ6oxbIjvN66P.jpg", 500, 6.0f),
    Movie(1, "Superman: Man of Tomorrow", "/6Bbq8qQWpoApLZYWFFAuZ1r2gFw.jpg", 1501, 7.0f)
)

internal val Store.Companion.mock
    get() = init(
        initialState = FavoriteFeatureState(
            favoriteMovies = setOf(0, 1),
            movies = movies.associateBy(com.sergeyfitis.moviekeeper.data.models.Movie::id)
        ),
        reducer = favoriteMoviesFeatureReducer,
        environment = Unit
    )

class MainActivity : AppCompatActivity() {
    private val TAG = "FavoriteMoviesFeatureTest"

    private val mockStore = Store.mock

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        val factory = MoviesFavoriteFragment.Factory(Store.mock)
        supportFragmentManager.fragmentFactory = factory
        super.onCreate(savedInstanceState)
        val container = FrameLayout(this)
        container.id = 1
        setContentView(container)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(container.id, MoviesFavoriteFragment(mockStore))
                .commitNow()
        }
    }
}