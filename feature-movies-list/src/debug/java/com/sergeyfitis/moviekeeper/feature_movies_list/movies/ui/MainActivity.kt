package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.sergeyfitis.moviekeeper.data.models.MoviesResponse
import com.sergeyfitis.moviekeeper.data.models.RemoteMovie
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.MoviesFragment
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.environment.MoviesFeatureEnvironment
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.reducer.moviesFeatureReducer
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.stateclass.MoviesFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.navigation.MovieListNavigation
import com.syaremych.composable_architecture.store.Store

/* Activity to work on the feature in isolation */
class MainActivity : AppCompatActivity() {
    private val TAG = "MoviesListFeatureTest"

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

    private val mockEnvironment = MoviesFeatureEnvironment(
        nowPlayingMovies = { MoviesResponse(movies) },
        upcomingMovies = { MoviesResponse(movies) },
        topRatedMovies = { MoviesResponse(movies) }
    )

    val navigation = object : MovieListNavigation {
        override fun openMovieDetails(movieId: Int) {
            Log.d(TAG, "openMovieDetails $movieId")
        }
    }

    private val featureStore =
        Store.init(
            initialState = MoviesFeatureState.init(),
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