package com.sergeyfitis.moviekeeper.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import androidx.navigation.fragment.navArgs
import com.sergeyfitis.moviekeeper.R
import com.sergeyfitis.moviekeeper.statemanagement.action.MovieDetailsAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.MovieDetailsState
import com.sergeyfitis.moviekeeper.statemanagement.store.*
import kotlinx.coroutines.launch

class MovieDetailsFragment(
    store: Store<MovieDetailsState, MovieDetailsAction>
) : Fragment(R.layout.fragment_movie_details) {

    private val liveStore = store.asLiveData()
    private val args by navArgs<MovieDetailsFragmentArgs>()

    init {
        lifecycleScope.launch {
            whenCreated {
                val movieId = args.movieId
                liveStore.send(MovieDetailsAction.GetBy(this, movieId))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

val movieDetailsReducer = fun(state: MovieDetailsState?, action: MovieDetailsAction): Reduced<MovieDetailsState, MovieDetailsAction> {
    return when(action) {
        is MovieDetailsAction.GetBy -> TODO()
        is MovieDetailsAction.Loaded -> reduced(
            value = MovieDetailsState(action.movie, action.isFavorite),
            effects = noEffects()
        )
    }
}
