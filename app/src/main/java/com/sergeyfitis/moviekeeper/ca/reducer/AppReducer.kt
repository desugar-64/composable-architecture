package com.sergeyfitis.moviekeeper.ca.reducer

import com.sergeyfitis.moviekeeper.ca.action.*
import com.sergeyfitis.moviekeeper.ca.appstate.*
import com.sergeyfitis.moviekeeper.ca.effect.loadGenres
import com.sergeyfitis.moviekeeper.ca.environment.AppEnvironment
import com.sergeyfitis.moviekeeper.data.models.dto.GenreDTO
import com.sergeyfitis.moviekeeper.feature_movie.environment.MovieFeatureEnvironment
import com.sergeyfitis.moviekeeper.feature_movie.reducer.movieFeatureReducer
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.reducer.favoriteMoviesFeatureReducer
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.environment.MoviesFeatureEnvironment
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.reducer.moviesFeatureReducer
import com.syaremych.composable_architecture.prelude.identity
import com.syaremych.composable_architecture.prelude.types.emptyList
import com.syaremych.composable_architecture.store.Reducer
import com.syaremych.composable_architecture.store.combine
import com.syaremych.composable_architecture.store.pullback
import com.syaremych.composable_architecture.store.reduced

private val genresReducer =
    Reducer<AppState, AppAction.Genres, AppEnvironment> { appState, genresAction, env ->
        when (genresAction) {
            AppAction.Genres.Load -> reduced(appState, loadGenres(env.moviesClient.genres))
            is AppAction.Genres.Loaded -> reduced(
                appState.copy(
                    genres = genresAction
                        .result
                        .fold(::emptyList, ::identity)
                        .associateBy(GenreDTO::id)
                )
            )
        }
    }

val appReducer: Reducer<AppState, AppAction, AppEnvironment> =
    Reducer.combine(
        genresReducer.pullback(
            value = AppState.identityLens,
            action = AppAction.genresAction,
            environment = ::identity
        ),
        moviesFeatureReducer.pullback(
            value = AppState.moviesFeatureState,
            action = AppAction.moviesFeatureAction,
            environment = { appEnvironment ->
                MoviesFeatureEnvironment(
                    nowPlayingMovies = appEnvironment.moviesClient.nowPlaying,
                    upcomingMovies = appEnvironment.moviesClient.upcoming,
                    topRatedMovies = appEnvironment.moviesClient.topRated
                )
            }
        ),
        movieFeatureReducer.pullback(
            value = AppState.movieFeatureState,
            action = AppAction.movieFeatureAction,
            environment = { MovieFeatureEnvironment }
        ),
        favoriteMoviesFeatureReducer.pullback(
            value = AppState.favoriteFeatureState,
            action = AppAction.favoriteFeatureAction,
            environment = { Unit }
        )
    )