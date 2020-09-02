package com.sergeyfitis.moviekeeper.feature_movie

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.sergeyfitis.moviekeeper.data.models.Movie
import com.sergeyfitis.moviekeeper.feature_movie.action.MovieFeatureAction
import com.sergeyfitis.moviekeeper.feature_movie.environment.MovieFeatureEnvironment
import com.sergeyfitis.moviekeeper.feature_movie.reducer.movieFeatureReducer
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieFeatureState
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.prelude.types.toOption
import com.syaremych.composable_architecture.store.Store

class MainActivity : AppCompatActivity() {

    private val movie = Movie(
        0, "The Movie", "", 1000, 5.5f
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

        val featureFragment = MovieDetailsFragment(
            store
        )

        supportFragmentManager.beginTransaction()
            .add(container.id, featureFragment)
            .commitNow()
    }
}