package com.sergeyfitis.moviekeeper.feature_movies_list.movies

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sergeyfitis.moviekeeper.feature_movies_list.R
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesViewAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.adapter.MoviesAdapter
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesViewState
import com.syaremych.composable_architecture.store.Store
import com.syaremych.composable_architecture.store.asLiveData

class MoviesFragment(
    store: Store<MoviesViewState, MoviesViewAction>
) : Fragment(R.layout.fragment_movies) {

    private lateinit var rvMovies: RecyclerView

    private val liveStore = store.asLiveData(releaseStoreWith = this as LifecycleOwner)

    init {
        lifecycleScope.launchWhenCreated {
            liveStore.send(MoviesViewAction.loadMovies(this))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvMovies = view.findViewById(R.id.rv_movies)
        rvMovies.layoutManager = LinearLayoutManager(requireContext())
        liveStore.observe(viewLifecycleOwner, Observer(::render))
    }

    private fun render(viewState: MoviesViewState) {
        rvMovies.adapter = MoviesAdapter(viewState.moviesState.movies) { movie ->
            liveStore.send(MoviesViewAction.openMovie(movie))
//            val destination = actionMoviesFragmentToMovieDetailsFragment(movie.id)
//            findNavController().navigate(Uri.parse("moviekeeper://movie/${movie.id}"))
        }
    }
}