package com.sergeyfitis.moviekeeper.feature_movie

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.sergeyfitis.moviekeeper.feature_movie.action.MovieAction
import com.sergeyfitis.moviekeeper.feature_movie.action.MovieViewAction
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieState
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieViewState
import com.sergeyfitis.moviekeeper.feature_movie.state.movieStateLens
import com.syaremych.composable_architecture.store.*
import kotlinx.android.synthetic.main.fragment_movie_details.*

class MovieDetailsFragment(
    store: Store<MovieViewState, MovieViewAction>
) : Fragment(R.layout.fragment_movie_details) {

    private val liveStore = store.asLiveData(releaseStoreWith = this as LifecycleOwner)

    private val args by navArgs<MovieDetailsFragmentArgs>() // ??? problem, the main nav graph is located in the app module

    init {
        lifecycleScope.launchWhenCreated {
            val movieId = args.movieId
            liveStore.send(MovieViewAction.details(this, movieId))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        liveStore.observe(viewLifecycleOwner, ::render)
    }

    private fun render(state: MovieViewState) {
        Log.d("MovieDetailsFragment", "render invoked")
        tv_movie.text = state.movieState.movie.title
    }
}

private val movieStateReducer =
    /*fun(state: MovieState, action: MovieAction): Reduced<MovieState, MovieAction> {
        return when (action) {
            is MovieAction.GetDetails -> com.syaremych.composable_architecture.store.reduced(
                value = state,
                effects = noEffects()
            ) // TODO: load cast, etc
            is MovieAction.Loaded -> com.syaremych.composable_architecture.store.reduced(
                value = state.copy(movie = action.movie),
                effects = noEffects()
            )
        }
    }*/
    Reducer<MovieState, MovieAction, Any> {value, action, environment ->
        return@Reducer reduced(value, noEffects())
    }


/*
val mvr = movieStateReducer.pullback(
    value = TODO(),
    action = TODO(),
    environment = TODO()
)
*/

/*
val movieViewReducer =
    pullback<MovieState, MovieViewState, MovieAction, MovieViewAction>(
        reducer = movieStateReducer,
        valueGet = MovieViewState.movieStateLens::get,
        valueSet = MovieViewState.movieStateLens::set,
        toLocalAction = MovieViewAction.moviePrism::get,
        toGlobalAction = { map(MovieViewAction.moviePrism::reverseGet) }
    )
*/
