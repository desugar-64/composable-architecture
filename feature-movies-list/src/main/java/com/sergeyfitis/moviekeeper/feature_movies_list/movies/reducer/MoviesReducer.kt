package com.sergeyfitis.moviekeeper.feature_movies_list.movies.reducer

import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.moviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.environment.MoviesFeatureEnvironment
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.moviesState
import com.syaremych.composable_architecture.prelude.id
import com.syaremych.composable_architecture.store.Reducer
import com.syaremych.composable_architecture.store.pullback

internal val moviesViewReducer =
    Reducer<MoviesState, MoviesAction, MoviesFeatureEnvironment> { state, action, environment ->
        when (action) {
            is MoviesAction.MovieTapped -> TODO()
            MoviesAction.LoadMovies -> TODO()
            is MoviesAction.MoviesLoaded -> TODO()
        }
    }

// Assemble big feature reducer from its tiny reducers that describes the feature
val moviesFeatureReducer: Reducer<MoviesFeatureState, MoviesFeatureAction, MoviesFeatureEnvironment> =
    moviesViewReducer.pullback(
        MoviesFeatureState.moviesState,
        MoviesFeatureAction.moviesAction,
        ::id
    )
