package com.sergeyfitis.moviekeeper.feature_movies_list.movies

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sergeyfitis.moviekeeper.data.models.Movie
import com.sergeyfitis.moviekeeper.data.models.MoviesResponse
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.environment.MoviesFeatureEnvironment
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.reducer.moviesFeatureReducer
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.state.MoviesFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.navigation.MovieListNavigation
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.store.Store
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MoviesFragmentTest {

    private val movies = listOf(
        Movie(0, "Rogue", "/uOw5JD8IlD546feZ6oxbIjvN66P.jpg", 500, 6.0f),
        Movie(1, "Superman: Man of Tomorrow", "/6Bbq8qQWpoApLZYWFFAuZ1r2gFw.jpg", 1501, 7.0f)
    )

    @Test
    fun testEventFragment() {
        val mockEnvironment = MoviesFeatureEnvironment {
            MoviesResponse(movies)
        }

        val navigation = object : MovieListNavigation {
            override fun openMovieDetails(movieId: Int) {
//                Log.d("TEST", "openMovieDetails $movieId")
            }
        }

        val featureStore =
            Store.init(
                initialState = MoviesFeatureState(
                    selectedMovie = Option.empty(),
                    movies = emptyList(),
                    favorites = setOf(0, 1)
                ),
                reducer = moviesFeatureReducer,
                environment = mockEnvironment
            )

        val featureFragment = MoviesFragment(featureStore, navigation)
        val scenario = launchFragmentInContainer {
            featureFragment
        }
        scenario.moveToState(Lifecycle.State.CREATED)
        Assert.assertTrue(true)
    }
}