package com.sergeyfitis.moviekeeper.feature_movie

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.sergeyfitis.moviekeeper.data.models.Category
import com.sergeyfitis.moviekeeper.data.models.dto.MovieDTO
import com.sergeyfitis.moviekeeper.feature_movie.action.MovieFeatureAction
import com.sergeyfitis.moviekeeper.feature_movie.environment.MovieFeatureEnvironment
import com.sergeyfitis.moviekeeper.feature_movie.reducer.movieFeatureReducer
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieFeatureState
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.prelude.types.toOption
import com.syaremych.composable_architecture.store.Store

class MainActivity : AppCompatActivity() {

    private val movie = MovieDTO(
        id = 0,
        title = "The Movie",
        poster = "",
        backdrop = "",
        voteCount = 1000,
        voteAverage = 5.5f,
        category = Category.TOP_RATED,
        genres = emptyList()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val container = FrameLayout(this)
        container.id = 1
        setContentView(container)

        val store: Store<Option<MovieFeatureState>, MovieFeatureAction> =
            Store.init(
                initialState = MovieFeatureState(
                    selectedMovie = movie,
                    favoriteMovies = emptySet()
                ).toOption(),
                reducer = movieFeatureReducer,
                environment = MovieFeatureEnvironment

            )

        val args = Bundle().apply {
            putInt("movieId", 0)
        }

        val featureFragment = MovieDetailsFragment(
            store
        ).apply {
            arguments = args
        }

        supportFragmentManager.beginTransaction()
            .add(container.id, featureFragment)
            .commitNow()
    }
}