package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.sergeyfitis.moviekeeper.data.models.Movie
import com.sergeyfitis.moviekeeper.data.models.MoviesResponse
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.MoviesFragment
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.environment.MoviesFeatureEnvironment
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.reducer.moviesFeatureReducer
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.stateclass.MoviesFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.navigation.MovieListNavigation
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.store.Store

/* Activity to work on the feature in isolation */
class MainActivity : AppCompatActivity() {
    private val TAG = "MoviesListFeatureTest"

    private val movies = listOf(
        Movie(0, "Rogue", "/uOw5JD8IlD546feZ6oxbIjvN66P.jpg", 500, 6.0f),
        Movie(1, "Superman: Man of Tomorrow", "/6Bbq8qQWpoApLZYWFFAuZ1r2gFw.jpg", 1501, 7.0f)
    )

    private val mockEnvironment = MoviesFeatureEnvironment {
        MoviesResponse(movies)
    }

    val navigation = object : MovieListNavigation {
        override fun openMovieDetails(movieId: Int) {
            Log.d(TAG, "openMovieDetails $movieId")
        }
    }

    private val featureStore =
        Store.init(
            initialState = MoviesFeatureState(
                selectedMovie = Option.empty(),
                movies = emptyList(),
                favorites = setOf(0)
            ),
            reducer = moviesFeatureReducer,
            environment = mockEnvironment
        )

    val featureFragment = MoviesFragment(featureStore, navigation)

    private val factory = object : FragmentFactory() {
        override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
            return featureFragment
        }
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = factory
        super.onCreate(savedInstanceState)
        val container = FrameLayout(this)
        container.id = 1
        setContentView(container)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(container.id, featureFragment)
                .commitNow()
        }
    }
}