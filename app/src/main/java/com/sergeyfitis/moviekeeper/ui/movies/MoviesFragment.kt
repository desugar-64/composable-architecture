package com.sergeyfitis.moviekeeper.ui.movies

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
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
import com.sergeyfitis.moviekeeper.prelude.pullback
import com.sergeyfitis.moviekeeper.prelude.types.rmap
import com.sergeyfitis.moviekeeper.statemanagement.action.AppAction.MoviesAction
import com.sergeyfitis.moviekeeper.statemanagement.action.MoviesViewAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.MoviesState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.MoviesViewState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.moviesStateLens
import com.sergeyfitis.moviekeeper.statemanagement.store.*
import com.sergeyfitis.moviekeeper.ui.movies.MoviesFragmentDirections.Companion.actionMoviesFragmentToMovieDetailsFragment
import com.sergeyfitis.moviekeeper.ui.movies.adapter.MoviesAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

class MoviesFragment(
    store: Store<MoviesViewState, MoviesViewAction>
) : Fragment(R.layout.fragment_movies) {

    private lateinit var rvMovies: RecyclerView

    private val liveStore = store.asLiveData()

    init {
        lifecycleScope.launchWhenCreated() {
            liveStore.send(MoviesViewAction.loadMovies(this))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvMovies = view.findViewById(R.id.rv_movies)
        rvMovies.layoutManager = LinearLayoutManager(requireContext())
        liveStore.observe(viewLifecycleOwner, ::render)
    }

    private fun render(viewState: MoviesViewState) {
        rvMovies.adapter = MoviesAdapter(viewState.moviesState.movies) { movie ->
            liveStore.send(MoviesViewAction.openMovie(movie))
            val destination = actionMoviesFragmentToMovieDetailsFragment(movie.id)
            findNavController().navigate(destination)
        }
    }
}

private val moviesStateReducer =
    fun (state: MoviesState, action: MoviesAction): Reduced<MoviesState, MoviesAction> {
        return when(action) {
            is MoviesAction.Load -> reduced(state, listOf(action.scope.loadMoviesEffect()))
            is MoviesAction.Loaded -> reduced(
                value = state.copy(movies = action.movies.fold({ emptyList<Movie>() }, ::id)),
                effects = noEffects()
            )
        }
    }

/*val moviesViewReducer =
    fun(viewState: MoviesViewState, action: MoviesViewAction): Reduced<MoviesViewState, MoviesViewAction> {
        return when (action) {
            is MoviesAction.Open -> reduced(
                value = state.copy(selectedMovie = action.movie.toOption()),
                effects = noEffects()
            )
        }
    }*/

val moviesViewReducer = pullback<MoviesState, MoviesViewState, MoviesAction, MoviesViewAction>(
    reducer = moviesStateReducer,
    valueGet = MoviesViewState.moviesStateLens::get,
    valueSet = MoviesViewState.moviesStateLens::set,
    toLocalAction = { MoviesViewAction.moviesPrism.get(this) },
    toGlobalAction = { map(MoviesViewAction.moviesPrism::reverseGet) }
)

fun CoroutineScope.loadMoviesEffect(): Effect<MoviesAction> {
    return httpClient()
        .dataTask<MoviesResponse>(this, getPopularMovies)
        .map { MoviesAction.Loaded(it.rmap(MoviesResponse::results)) }
        .receiveOn(Dispatchers.Main.asExecutor())
}