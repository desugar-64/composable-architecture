package com.sergeyfitis.moviekeeper.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.sergeyfitis.moviekeeper.R
import com.sergeyfitis.moviekeeper.statemanagement.action.MovieDetailsAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.MovieDetailsState
import com.sergeyfitis.moviekeeper.statemanagement.store.Reduced
import com.sergeyfitis.moviekeeper.statemanagement.store.Store
import com.sergeyfitis.moviekeeper.statemanagement.store.asLiveData

class MovieDetailsFragment(
    store: Store<MovieDetailsState, MovieDetailsAction>
) : Fragment(R.layout.fragment_movie_details) {

    private val store = store.asLiveData()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

val movieDetailsReducer = fun(state: MovieDetailsState?, action: MovieDetailsAction): Reduced<MovieDetailsState, MovieDetailsAction> {
    TODO()
}
