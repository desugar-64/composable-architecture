package com.sergeyfitis.moviekeeper.ui.movies

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sergeyfitis.moviekeeper.R
import com.sergeyfitis.moviekeeper.effects.dataTask
import com.sergeyfitis.moviekeeper.effects.getPopularMovies
import com.sergeyfitis.moviekeeper.effects.httpClient
import com.sergeyfitis.moviekeeper.models.Movie
import com.sergeyfitis.moviekeeper.models.MoviesResponse
import com.sergeyfitis.moviekeeper.statemanagement.action.MoviesAction
import com.sergeyfitis.moviekeeper.statemanagement.store.*
import com.sergeyfitis.moviekeeper.ui.movies.adapter.MoviesAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class MoviesFragment(
    store: Store<List<Movie>, MoviesAction>
) : Fragment() {

    lateinit var rvMovies: RecyclerView

    private val store = store.asLiveData()

    init {
        lifecycleScope.launch {
            whenStarted { store.send(MoviesAction.Load(this)) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_movies, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvMovies = view.findViewById(R.id.rv_movies)
        rvMovies.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        store.observe(viewLifecycleOwner, Observer {
            rvMovies.adapter = MoviesAdapter(it.map(Movie::title)) { selectedMovie ->
                findNavController().navigate(R.id.movieDetailsFragment)
            }
        })
    }
}

val moviesReducer = fun(movies: List<Movie>, action: MoviesAction): Reduced<List<Movie>, MoviesAction> {
    return when(action) {
        is MoviesAction.Load -> reduced(movies, listOf(action.scope.loadMoviesEffect()))
        is MoviesAction.Loaded -> reduced(action.movies, noEffects())
    }
}

fun CoroutineScope.loadMoviesEffect(): Effect<MoviesAction> {
    return httpClient()
        .dataTask<MoviesResponse>(this, getPopularMovies)
        .map { MoviesAction.Loaded(it.results) }
        .receiveOn(mainThread)
}

val mainThread = Executor { Handler(Looper.getMainLooper()).post(it) }