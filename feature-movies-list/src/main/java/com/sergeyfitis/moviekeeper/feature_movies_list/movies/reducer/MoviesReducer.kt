package com.sergeyfitis.moviekeeper.feature_movies_list.movies.reducer

import com.sergeyfitis.moviekeeper.data.models.Movie
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.effects.loadMoviesEffect
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.ViewState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.moviesState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.moviesStateGetter
import com.syaremych.composable_architecture.prelude.id
import com.syaremych.composable_architecture.store.Reducer
import com.syaremych.composable_architecture.store.noEffects
import com.syaremych.composable_architecture.store.pullback
import com.syaremych.composable_architecture.store.reduced


private val moviesFeatureReducer =
    Reducer { featureState: ViewState, action: MoviesFeatureAction, environment: Any ->
        return@Reducer when(action) {
            is MoviesFeatureAction.Load -> reduced(
                featureState,
                listOf(action.scope.loadMoviesEffect())
            )
            is MoviesFeatureAction.Loaded -> reduced(
                value = featureState.copy(movies = action.movies.fold({ emptyList<Movie>() }, ::id)),
                effects = noEffects()
            )
            is MoviesFeatureAction.Open -> reduced(
                value = featureState/*featureState.copy(selectedMovie = action.movie.toOption())*/, // TODO: Move to view state
                effects = noEffects()
            )
        }
    }
    /*fun (featureState: MoviesFeatureState, action: MoviesAction): Reduced<MoviesFeatureState, MoviesAction> {
        return when(action) {
            is MoviesAction.Load -> reduced(
                featureState,
                listOf(action.scope.loadMoviesEffect())
            )
            is MoviesAction.Loaded -> reduced(
                value = featureState.copy(movies = action.movies.fold({ emptyList<Movie>() }, ::id)),
                effects = noEffects()
            )
            is MoviesAction.Open -> reduced(
                value = featureState.copy(selectedMovie = action.movie.toOption()),
                effects = noEffects()
            )
        }
    }*/

val moviesViewReducer =
    moviesFeatureReducer.pullback(
        value = MoviesFeatureState.moviesState,
        action = MoviesFeatureAction.movieAction
    )
/*pullback<MoviesFeatureState, ViewState, MoviesAction, MoviesViewAction>(
    reducer = moviesFeatureReducer,
    valueGet = ViewState.moviesStateGetter::get,
    valueSet = ViewState.moviesStateGetter::set,
    toLocalAction = MoviesViewAction.moviesActionPrism::get,
    toGlobalAction = { map(MoviesViewAction.moviesActionPrism::reverseGet) }
)*/