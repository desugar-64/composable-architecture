package com.sergeyfitis.moviekeeper.ui.movies

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.lifecycle.whenCreated
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sergeyfitis.moviekeeper.R
import com.sergeyfitis.moviekeeper.effects.dataTask
import com.sergeyfitis.moviekeeper.effects.getPopularMovies
import com.sergeyfitis.moviekeeper.effects.httpClient
import com.sergeyfitis.moviekeeper.models.Movie
import com.sergeyfitis.moviekeeper.models.MoviesResponse
import com.sergeyfitis.moviekeeper.prelude.id
import com.sergeyfitis.moviekeeper.prelude.types.rmap
import com.sergeyfitis.moviekeeper.statemanagement.action.MoviesAction
import com.sergeyfitis.moviekeeper.statemanagement.store.*
import com.sergeyfitis.moviekeeper.ui.movies.MoviesFragmentDirections.Companion.actionMoviesFragmentToMovieDetailsFragment
import com.sergeyfitis.moviekeeper.ui.movies.adapter.MoviesAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.launch

class MoviesFragment(
    store: Store<List<Movie>, MoviesAction>
) : Fragment(R.layout.fragment_movies) {

    private lateinit var rvMovies: RecyclerView

    private val liveStore = store.asLiveData()

    init {
        lifecycleScope.launch {
            whenCreated { liveStore.send(MoviesAction.Load(this)) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvMovies = view.findViewById(R.id.rv_movies)
        rvMovies.layoutManager = LinearLayoutManager(requireContext())
        liveStore.observe(viewLifecycleOwner, ::render)
    }

    private fun render(movies: List<Movie>) {
        rvMovies.adapter = MoviesAdapter(movies) { movie ->
            val destination = actionMoviesFragmentToMovieDetailsFragment(movie.id)
            findNavController().navigate(destination)
        }
    }
}

val moviesReducer =
    fun(movies: List<Movie>, action: MoviesAction): Reduced<List<Movie>, MoviesAction> {
        return when (action) {
            is MoviesAction.Load -> reduced(movies, listOf(action.scope.loadMoviesEffect()))
            is MoviesAction.Loaded -> reduced(
                value = action.movies.fold({ emptyList<Movie>() }, ::id),
                effects = noEffects()
            )
        }
    }

fun CoroutineScope.loadMoviesEffect(): Effect<MoviesAction> {
    return httpClient()
        .dataTask<MoviesResponse>(this, getPopularMovies)
        .map { MoviesAction.Loaded(it.rmap(MoviesResponse::results)) }
        .receiveOn(Dispatchers.Main.asExecutor())
}