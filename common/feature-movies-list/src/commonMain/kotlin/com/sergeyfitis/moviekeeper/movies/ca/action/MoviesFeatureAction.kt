package com.sergeyfitis.moviekeeper.movies.ca.action

import com.syaremych.composable_architecture.prelude.types.Prism

sealed class MoviesFeatureAction {
    data class Movies(val action: MoviesAction) : MoviesFeatureAction()

    companion object
}

internal val MoviesFeatureAction.Companion.moviesAction: Prism<MoviesFeatureAction, MoviesAction>
    get() = Prism(
        get = { featureAction ->
            when (featureAction) {
                is MoviesFeatureAction.Movies -> featureAction.action
            }
        },
        reverseGet = { viewAction ->
            MoviesFeatureAction.Movies(viewAction)
        }
    )