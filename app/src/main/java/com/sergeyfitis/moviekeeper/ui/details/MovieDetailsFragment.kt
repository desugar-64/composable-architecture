package com.sergeyfitis.moviekeeper.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sergeyfitis.moviekeeper.R
import com.sergeyfitis.moviekeeper.statemanagement.action.MovieDetailsAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.MovieDetailsState
import com.sergeyfitis.moviekeeper.statemanagement.store.Effect
import com.sergeyfitis.moviekeeper.statemanagement.store.Store

class MovieDetailsFragment(
    private val store: Store<MovieDetailsState, MovieDetailsAction>
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }
}

val movieDetailsReducer = fun(state: MovieDetailsState?, action: MovieDetailsAction): List<Pair<MovieDetailsState, Effect<MovieDetailsAction>>> {
    TODO()
}
