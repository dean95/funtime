/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
import java.util.*
import java.util.concurrent.TimeUnit

class MainAdapter : RecyclerView.Adapter<MainAdapter.MovieViewHolder>() {

  private val onMovieClickSubject: Subject<ApiMovie> = BehaviorSubject.create()

  private var movies: List<ApiMovie> = ArrayList()

  companion object {
    const val CLICK_THROTTLE_WINDOW_MILLIS = 300L
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

  fun onItemClick() = onMovieClickSubject
      .throttleFirst(CLICK_THROTTLE_WINDOW_MILLIS, TimeUnit.SECONDS)

  class MovieViewHolder(val view: View,
                        private val clickSubject: Subject<ApiMovie>) : RecyclerView.ViewHolder(view) {

    private lateinit var movie: ApiMovie

    fun setMovie(movie: ApiMovie) {
      this.movie = movie
      with(movie) {
        itemView.tv_main_movie_title.text = title
        itemView.tv_main_movie_year.text = year.toString()
        itemView.main_movie_item_container.setOnClickListener { onMovieClick() }
      }
    }

    private fun onMovieClick() = clickSubject.onNext(movie)
  }
}