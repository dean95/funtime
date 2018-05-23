package com.raywenderlich.funtime.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.raywenderlich.funtime.R
import com.raywenderlich.funtime.data.network.model.ApiMovie
import com.raywenderlich.funtime.data.network.model.ApiMoviesResult
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.movie_item_view.view.*
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class MainAdapter: RecyclerView.Adapter<MainAdapter.MovieViewHolder>() {

    private val onMovieClickSubject: Subject<ApiMovie> = BehaviorSubject.create()

    private var movies: List<ApiMovie> = ArrayList()

    companion object {
        val CLICK_THROTTLE_WINDOW_MILLIS = 300L;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.movie_item_view, parent, false)
        return MovieViewHolder(itemView, onMovieClickSubject)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.setMovie(movies[position])
    }

    fun onMoviesUpdate(movies: ApiMoviesResult) {
        this.movies = movies.items
        notifyDataSetChanged()
    }

    fun onItemClick() = onMovieClickSubject.throttleFirst(CLICK_THROTTLE_WINDOW_MILLIS, TimeUnit.SECONDS)

    class MovieViewHolder(val view: View, val clickSubject: Subject<ApiMovie>): RecyclerView.ViewHolder(view) {

        private lateinit var movie: ApiMovie

        fun setMovie(movie: ApiMovie) {
            this.movie = movie
            with(movie) {
                itemView.tv_main_movie_title.text = title
                itemView.tv_main_movie_year.text = year.toString()
            }
        }

        fun onMovieClick() = clickSubject.onNext(movie)
    }
}