package com.sergeyfitis.moviekeeper.feature_movies_list.movies.reducer

import com.sergeyfitis.moviekeeper.data.models.Movie
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesViewAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.effects.loadMoviesEffect
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesViewState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.moviesStateLens
import com.syaremych.composable_architecture.prelude.id
import com.syaremych.composable_architecture.prelude.pullback
import com.syaremych.composable_architecture.prelude.types.toOption
import com.syaremych.composable_architecture.store.Reduced
import com.syaremych.composable_architecture.store.noEffects
import com.syaremych.composable_architecture.store.reduced


private val moviesStateReducer =
    fun (state: MoviesState, action: MoviesAction): Reduced<MoviesState, MoviesAction> {
        return when(action) {
            is MoviesAction.Load -> reduced(
                state,
                listOf(action.scope.loadMoviesEffect())
            )
            is MoviesAction.Loaded -> reduced(
                value = state.copy(movies = action.movies.fold({ emptyList<Movie>() }, ::id)),
                effects = noEffects()
            )
            is MoviesAction.Open -> reduced(
                value = state.copy(selectedMovie = action.movie.toOption()),
                effects = noEffects()
            )
        }
    }

val moviesViewReducer = pullback<MoviesState, MoviesViewState, MoviesAction, MoviesViewAction>(
    reducer = moviesStateReducer,
    valueGet = MoviesViewState.moviesStateLens::get,
    valueSet = MoviesViewState.moviesStateLens::set,
    toLocalAction = MoviesViewAction.moviesActionPrism::get,
    toGlobalAction = { map(MoviesViewAction.moviesActionPrism::reverseGet) }
)