package com.sergeyfitis.moviekeeper.data.models

import com.sergeyfitis.moviekeeper.data.models.dto.GenreDTO
import kotlinx.serialization.Serializable

@Serializable
data class RemoteGenre(
    val id: Int,
    val name: String
)

fun RemoteGenre.toDTO() = GenreDTO(
    id = id,
    name = name
)