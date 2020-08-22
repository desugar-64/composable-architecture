package com.sergeyfitis.moviekeeper.feature_movies_list.movies

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sergeyfitis.moviekeeper.feature_movies_list.R
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.adapter.MoviesAdapter
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.navigation.MovieListNavigation
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesState
import com.syaremych.composable_architecture.store.Store
import com.syaremych.composable_architecture.store.ViewStore
import com.syaremych.composable_architecture.store.view

class MoviesFragment(
    store: Store<MoviesFeatureState, MoviesFeatureAction>,
    private val navigator: MovieListNavigation
) : Fragment(R.layout.fragment_movies) {

    private lateinit var rvMovies: RecyclerView

    private val viewStore: ViewStore<MoviesState, MoviesAction> =
        store.scope<MoviesState, MoviesAction>(
            toLocalValue = { TODO() },
            toGlobalAction = { TODO() }
        ).view


    private val subscriber: Store.Subscriber<MoviesState> = object : Store.Subscriber<MoviesState> {
        override fun render(value: MoviesState) {
            rvMovies.adapter = MoviesAdapter(value.movies) { movie ->
                viewStore.send(MoviesAction.MovieTapped(movie))
                navigator.openMovieDetails(movie)
            }
        }
    }

    init {
        lifecycleScope.launchWhenCreated {
            viewStore.send(MoviesAction.LoadMovies(this))
        }
        lifecycle.addObserver(viewStore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvMovies = view.findViewById(R.id.rv_movies)
        rvMovies.layoutManager = LinearLayoutManager(requireContext())

        viewStore.subscribe(subscriber)
    }

    override fun onDestroyView() {
        viewStore.unsubscribe(subscriber)
        super.onDestroyView()
    }

}