package com.sergeyfitis.moviekeeper.feature_movie.action

import com.syaremych.composable_architecture.prelude.types.Prism

sealed class MovieFeatureAction {
    class Movie(val action: MovieAction) : MovieFeatureAction()

    companion object
}

val MovieFeatureAction.Companion.movieAction: Prism<MovieFeatureAction, MovieAction>
    get() = Prism(
        get = { featureAction -> when(featureAction) {
            is MovieFeatureAction.Movie -> featureAction.action
        } },
        reverseGet = MovieFeatureAction::Movie
    )