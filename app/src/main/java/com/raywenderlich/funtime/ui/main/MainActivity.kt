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

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.raywenderlich.funtime.R
import com.raywenderlich.funtime.data.network.model.ApiMovie
import com.raywenderlich.funtime.data.network.model.ApiMoviesResult
import com.raywenderlich.funtime.ui.trailer.TrailerActivity

class MainActivity : AppCompatActivity(), MainContract.View {

  private lateinit var moviesRecyclerView: RecyclerView
  private lateinit var presenter: MainContract.Presenter
  private val mainAdapter = MainAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    init()
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.deactivate()
  }

  override fun moviesFetchedSuccessfully(movies: ApiMoviesResult) {
    mainAdapter.onMoviesUpdate(movies)
  }

  override fun moviesFetchFailed(throwable: Throwable) {
    Toast.makeText(this, getString(R.string.main_error_message), Toast.LENGTH_SHORT).show()
  }

  fun onRefreshButtonClick(view: View) {
    presenter.fetchMovies()
  }

  private fun init() {
    moviesRecyclerView = findViewById(R.id.rv_main_movies_container)
    presenter = MainPresenter(this)

    initializeRecyclerView()

    presenter.fetchMovies()
  }

  private fun initializeRecyclerView() {
    moviesRecyclerView.setHasFixedSize(true)
    moviesRecyclerView.layoutManager = LinearLayoutManager(this)
    moviesRecyclerView.adapter = mainAdapter
    mainAdapter.onItemClick()
        .subscribe(this::onMovieClick)
  }

  private fun onMovieClick(movie: ApiMovie) {
    val intent = Intent(this, TrailerActivity::class.java)
    intent.putExtra(TrailerActivity.MOVIE_ID_EXTRA, movie.id)
    startActivity(intent)
  }
}
