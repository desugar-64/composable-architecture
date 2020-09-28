package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.reducer

import com.sergeyfitis.moviekeeper.data.models.Category
import com.sergeyfitis.moviekeeper.data.models.MovieDTO
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.moviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.effects.loadNowPlayingEffect
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.effects.loadTopRatedEffect
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.effects.loadUpcomingEffect
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.environment.MoviesFeatureEnvironment
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.stateclass.MoviesFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.stateclass.MoviesState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.stateclass.moviesState
import com.syaremych.composable_architecture.prelude.identity
import com.syaremych.composable_architecture.prelude.types.Either
import com.syaremych.composable_architecture.prelude.types.emptyList
import com.syaremych.composable_architecture.prelude.types.getOption
import com.syaremych.composable_architecture.store.*
import kotlinx.coroutines.Dispatchers

internal val moviesViewReducer =
    Reducer<MoviesState, MoviesAction, MoviesFeatureEnvironment> { state, action, environment ->
        when (action) {
            is MoviesAction.MovieTapped -> reduced(
                value = state.copy(selectedMovie = state.movies.getOption(action.movieId)),
                effects = noEffects()
            ) // navigation to the screen details
            MoviesAction.LoadMovies -> {
                reduced(
                    value = state,
                    effects = listOf(
                        Effect.merge(
                            loadNowPlayingEffect(environment.nowPlayingMovies),
                            loadUpcomingEffect(environment.upcomingMovies),
                            loadTopRatedEffect(environment.topRatedMovies),
                        ).runOn(Dispatchers.IO)
                    )
                )
            }
            is MoviesAction.MoviesLoaded -> reduced(state.updateMovies(action.result), noEffects())
        }
    }

// Assemble a big feature reducer from its tiny reducers that describes the feature
val moviesFeatureReducer: Reducer<MoviesFeatureState, MoviesFeatureAction, MoviesFeatureEnvironment> =
    moviesViewReducer.pullback(
        value = MoviesFeatureState.moviesState,
        action = MoviesFeatureAction.moviesAction,
        environment = ::identity
    )


private fun MoviesState.updateMovies(result: Either<Throwable, List<MovieDTO>>): MoviesState {
    val list = result.fold(::emptyList, ::identity)
    if (list.isEmpty()) return this
    val moviesMap = list.associateBy(MovieDTO::id)
    val stateMovies = movies + moviesMap
    return when (list.first().category) {
        Category.NOW_PLAYING -> copy(nowPlaying = moviesMap.keys, movies = stateMovies)
        Category.UPCOMING -> copy(upcoming = moviesMap.keys, movies = stateMovies)
        Category.TOP_RATED -> copy(topRated = moviesMap.keys, movies = stateMovies)
    }
}