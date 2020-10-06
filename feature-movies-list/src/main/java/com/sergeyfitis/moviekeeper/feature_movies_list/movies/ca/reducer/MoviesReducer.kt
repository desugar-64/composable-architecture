package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.reducer

import com.sergeyfitis.moviekeeper.data.models.Category
import com.sergeyfitis.moviekeeper.data.models.dto.MovieDTO
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

internal val moviesViewReducer =
    Reducer<MoviesState, MoviesAction, MoviesFeatureEnvironment> { state, action, environment ->
        when (action) {
            is MoviesAction.MovieTapped -> reduced<MoviesState, MoviesAction>(
                value = state.copy(selectedMovie = state.movies.getOption(action.movieId))
            )
            MoviesAction.LoadMovies -> reduced(
                value = state,
                effect = Effect.merge(
                    loadNowPlayingEffect(environment.nowPlayingMovies),
                    loadUpcomingEffect(environment.upcomingMovies),
                    loadTopRatedEffect(environment.topRatedMovies)
                )
            )
            is MoviesAction.MoviesLoaded -> reduced<MoviesState, MoviesAction>(
                value = state.updateMovies(action.result)
            )
        }
        // navigation to the screen details
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