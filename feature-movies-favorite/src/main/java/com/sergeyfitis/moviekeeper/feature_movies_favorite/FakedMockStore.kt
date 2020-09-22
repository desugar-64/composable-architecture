package com.sergeyfitis.moviekeeper.feature_movies_favorite

import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.action.FavoriteFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.state.FavoriteFeatureState
import com.syaremych.composable_architecture.prelude.absurd
import com.syaremych.composable_architecture.store.Store

val Store.Companion.mock: Store<FavoriteFeatureState, FavoriteFeatureAction>
    get() = absurd(Unit)