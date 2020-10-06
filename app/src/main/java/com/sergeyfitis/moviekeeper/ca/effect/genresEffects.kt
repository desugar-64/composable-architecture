package com.sergeyfitis.moviekeeper.ca.effect

import com.sergeyfitis.moviekeeper.ca.action.AppAction
import com.sergeyfitis.moviekeeper.data.models.GenresResponse
import com.sergeyfitis.moviekeeper.data.models.RemoteGenre
import com.sergeyfitis.moviekeeper.data.models.toDTO
import com.syaremych.composable_architecture.prelude.types.Either
import com.syaremych.composable_architecture.prelude.types.recover
import com.syaremych.composable_architecture.prelude.types.rmap
import com.syaremych.composable_architecture.store.Effect
import com.syaremych.composable_architecture.store.eraseToEffect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

fun loadGenres(getGenres: suspend () -> GenresResponse): Effect<AppAction.Genres.Loaded> =
    flow {
        kotlinx.coroutines.delay(5000)
        emit(Either.recover { getGenres() })
    }
        .map { it.rmap(GenresResponse::genres) }
        .map { it.rmap { list -> list.map(RemoteGenre::toDTO) } }
        .map(AppAction.Genres::Loaded)
        .eraseToEffect()