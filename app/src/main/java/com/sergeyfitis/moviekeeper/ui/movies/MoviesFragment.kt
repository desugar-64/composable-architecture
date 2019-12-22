package com.sergeyfitis.moviekeeper.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sergeyfitis.moviekeeper.R
import com.sergeyfitis.moviekeeper.statemanagement.action.AppAction
import com.sergeyfitis.moviekeeper.statemanagement.action.MoviesAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.AppState
import com.sergeyfitis.moviekeeper.statemanagement.store.Store
import com.sergeyfitis.moviekeeper.ui.movies.adapter.MoviesAdapter

class MoviesFragment(
    private val store: Store<AppState, AppAction>
) : Fragment(), Store.Subscriber<AppState> {

    lateinit var rvMovies: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_movies, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvMovies = view.findViewById(R.id.rv_movies)
        rvMovies.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        store.subscribe(this)
        store.send(MoviesAction.Load)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        store.unsubscribe(this)
    }

    override fun render(value: AppState) {
        rvMovies.adapter = MoviesAdapter(value.movies) { selectedMovie ->
            findNavController().navigate(R.id.movieDetailsFragment)
        }
    }
}

val moviesReducer = fun(movies: MutableList<String>, action: MoviesAction) {
    when(action) {
        MoviesAction.Load -> { /* ignore for now */ }
        is MoviesAction.Loaded -> movies.apply { clear(); addAll(getMovies()) }
    }
}

private fun getMovies(): List<String> {
    return listOf(
        "Parasite (2019)",
        "The Irishman (2019)",
        "Burning (2018)",
        "Long Day's Journey Into Night (2018)",
        "Birds of Passage (2018)"
    )
}