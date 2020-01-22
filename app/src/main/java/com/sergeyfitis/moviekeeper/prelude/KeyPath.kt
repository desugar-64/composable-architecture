package com.sergeyfitis.moviekeeper.prelude

open class KeyPath<Root, Value>(
    val extract: (Root) -> Value?,
    val embed: (Root, Value) -> Root
)

/* Usage example
class MoviePosterKeyPath : KeyPath<Movie, String>(
    extract = { it.poster },
    embed = { movie, poster -> movie.copy(poster = poster) }
)

fun Movie.Companion.posterKeyPath() = MoviePosterKeyPath() // aka Movie::poster
*/
