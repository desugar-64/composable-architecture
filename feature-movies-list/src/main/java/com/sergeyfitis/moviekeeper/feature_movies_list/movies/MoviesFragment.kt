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
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.adapter.MoviesAdapter
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.navigation.MovieListNavigation
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesState
import com.syaremych.composable_architecture.store.Store
import com.syaremych.composable_architecture.store.asLiveData

class MoviesFragment(
    store: Store<MoviesState, MoviesAction>,
    private val navigator: MovieListNavigation
) : Fragment(R.layout.fragment_movies) {

    private lateinit var rvMovies: RecyclerView

    private val liveStore = store.asLiveData(releaseStoreWith = this as LifecycleOwner)

    init {
        lifecycleScope.launchWhenCreated {
            liveStore.send(MoviesAction.loadMovies(this))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvMovies = view.findViewById(R.id.rv_movies)
        rvMovies.layoutManager = LinearLayoutManager(requireContext())
        liveStore.observe(viewLifecycleOwner, Observer(::render))
    }

    private fun render(viewState: MoviesState) {
        rvMovies.adapter = MoviesAdapter(viewState.moviesFeatureState.movies) { movie ->
            liveStore.send(MoviesAction.loadMovie(movie))
            navigator.openMovieDetails(movie)
        }
    }
}