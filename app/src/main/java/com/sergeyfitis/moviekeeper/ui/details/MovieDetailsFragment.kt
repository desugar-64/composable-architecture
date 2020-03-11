package com.sergeyfitis.moviekeeper.ui.details

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.sergeyfitis.moviekeeper.R
import com.sergeyfitis.moviekeeper.statemanagement.action.MovieAction
import com.sergeyfitis.moviekeeper.statemanagement.action.MovieViewAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.MovieState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.MovieViewState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.movieStateLens
import com.syaremych.composable_architecture.prelude.pullback
import com.syaremych.composable_architecture.store.asLiveData
import kotlinx.android.synthetic.main.fragment_movie_details.*

class MovieDetailsFragment(
    store: com.syaremych.composable_architecture.store.Store<MovieViewState, MovieViewAction>
) : Fragment(R.layout.fragment_movie_details) {

    private val liveStore = store.asLiveData()
    private val args by navArgs<MovieDetailsFragmentArgs>()

    init {
        lifecycleScope.launchWhenCreated {
            val movieId = args.movieId
            liveStore.send(MovieViewAction.getDetails(this, movieId))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        liveStore.observe(viewLifecycleOwner, ::render)
    }

    private fun render(state: MovieViewState) {
        Log.d("MovieDetailsFragment", "render invoked")
        tv_movie.text = state.movieState.movie.title
    }
}

private val movieStateReducer =
    fun(state: MovieState, action: MovieAction): com.syaremych.composable_architecture.store.Reduced<MovieState, MovieAction> {
        return when (action) {
            is MovieAction.GetDetails -> com.syaremych.composable_architecture.store.reduced(
                value = state,
                effects = com.syaremych.composable_architecture.store.noEffects()
            ) // TODO: load cast, etc
            is MovieAction.Loaded -> com.syaremych.composable_architecture.store.reduced(
                value = state.copy(movie = action.movie),
                effects = com.syaremych.composable_architecture.store.noEffects()
            )
        }
    }

val movieViewReducer =
    pullback<MovieState, MovieViewState, MovieAction, MovieViewAction>(
        reducer = movieStateReducer,
        valueGet = MovieViewState.movieStateLens::get,
        valueSet = MovieViewState.movieStateLens::set,
        toLocalAction = MovieViewAction.moviePrism::get,
        toGlobalAction = { map(MovieViewAction.moviePrism::reverseGet) }
    )
