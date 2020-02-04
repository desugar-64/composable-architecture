package com.sergeyfitis.moviekeeper.ui.movies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sergeyfitis.moviekeeper.models.Movie

typealias OnMovieItemClicked = (movie: Movie) -> Unit

class MoviesAdapter(
    private val movies: List<Movie>,
    private val onMovieItemClicked: OnMovieItemClicked
) : RecyclerView.Adapter<MoviesHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesHolder {
        return MoviesHolder(
            LayoutInflater.from(parent.context).inflate(
                android.R.layout.simple_list_item_1,
                parent,
                false
            ),
            onMovieItemClicked
        )
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: MoviesHolder, position: Int) {
        holder.bind(movies[position])
    }

}

class MoviesHolder(
    itemView: View,
    onMovieItemClicked: OnMovieItemClicked
) : RecyclerView.ViewHolder(itemView) {
    private val title: TextView = itemView.findViewById(android.R.id.text1)

    private lateinit var movie: Movie

    init {
        title.setOnClickListener { onMovieItemClicked(movie) }
    }

    fun bind(movie: Movie) {
        this.movie = movie
        title.text = movie.title
    }
}
