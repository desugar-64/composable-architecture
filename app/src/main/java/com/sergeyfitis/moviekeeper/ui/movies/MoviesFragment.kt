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
import com.sergeyfitis.moviekeeper.statemanagement.action.MoviesAction
import com.sergeyfitis.moviekeeper.statemanagement.store.Effect
import com.sergeyfitis.moviekeeper.statemanagement.store.Store
import com.sergeyfitis.moviekeeper.statemanagement.store.noEffect
import com.sergeyfitis.moviekeeper.statemanagement.store.reduced
import com.sergeyfitis.moviekeeper.ui.movies.adapter.MoviesAdapter

class MoviesFragment(
    private val store: Store<List<String>, MoviesAction>
) : Fragment(), Store.Subscriber<List<String>> {

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

    override fun render(value: List<String>) {
        rvMovies.adapter = MoviesAdapter(value) { selectedMovie ->
            findNavController().navigate(R.id.movieDetailsFragment)
        }
    }
}

val moviesReducer = fun(movies: List<String>, action: MoviesAction): List<Pair<List<String>, Effect<MoviesAction>>> {
    return when(action) {
        MoviesAction.Load -> emptyList()
        is MoviesAction.Loaded -> return reduced(getMovies(), noEffect())
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