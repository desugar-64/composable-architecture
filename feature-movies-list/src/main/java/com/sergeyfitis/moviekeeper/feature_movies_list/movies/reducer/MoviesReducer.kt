package com.sergeyfitis.moviekeeper.feature_movies_list.movies.reducer

import com.sergeyfitis.moviekeeper.data.models.Movie
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.moviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.effects.loadMoviesEffect
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.environment.MoviesFeatureEnvironment
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.moviesState
import com.syaremych.composable_architecture.prelude.id
import com.syaremych.composable_architecture.store.*

internal val moviesViewReducer =
    Reducer<MoviesState, MoviesAction, MoviesFeatureEnvironment> { state, action, environment ->
        when(action) {
            is MoviesAction.MovieTapped -> TODO()
        }
    }
/*pullback<MoviesFeatureState, ViewState, MoviesAction, MoviesViewAction>(
    reducer = moviesFeatureReducer,
    valueGet = ViewState.moviesStateGetter::get,
    valueSet = ViewState.moviesStateGetter::set,
    toLocalAction = MoviesViewAction.moviesActionPrism::get,
    toGlobalAction = { map(MoviesViewAction.moviesActionPrism::reverseGet) }
)*/

// Assemble big feature reducer from its tiny reducers that describes the feature
val moviesFeatureReducer =
    Reducer.combine(
        moviesViewReducer.pullback(MoviesFeatureState.moviesState, MoviesFeatureAction.moviesAction, ::id))

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
