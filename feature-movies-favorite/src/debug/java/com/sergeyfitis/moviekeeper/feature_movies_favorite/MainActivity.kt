package com.sergeyfitis.moviekeeper.feature_movies_favorite

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.sergeyfitis.moviekeeper.data.models.Category
import com.sergeyfitis.moviekeeper.data.models.RemoteMovie
import com.sergeyfitis.moviekeeper.data.models.dto.MovieDTO
import com.sergeyfitis.moviekeeper.data.models.toDTO
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.reducer.favoriteMoviesFeatureReducer
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.state.FavoriteFeatureState
import com.syaremych.composable_architecture.store.Store

private val movies = listOf(
    RemoteMovie(
        id = 0,
        title = "Rogue",
        poster = "/uOw5JD8IlD546feZ6oxbIjvN66P.jpg",
        backdrop = "",
        voteCount = 500,
        voteAverage = 6.0f,
        genres = emptyList(),
        overview = "A professional thief with \$40 million in debt and his family's life on the line must commit one final heist - rob a futuristic airborne casino filled with the world's most dangerous criminals."
    ),
    RemoteMovie(
        id = 1,
        title = "Superman: Man of Tomorrow",
        poster = "/6Bbq8qQWpoApLZYWFFAuZ1r2gFw.jpg",
        backdrop = "",
        voteCount = 1501,
        voteAverage = 7.0f,
        genres = emptyList(),
        overview = "A professional thief with \$40 million in debt and his family's life on the line must commit one final heist - rob a futuristic airborne casino filled with the world's most dangerous criminals."
    )
)

internal val Store.Companion.mock
    get() = init(
        initialState = FavoriteFeatureState(
            favoriteMovies = setOf(0, 1),
            movies = movies.map { it.toDTO(Category.TOP_RATED) }
                .associateBy(MovieDTO::id)
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