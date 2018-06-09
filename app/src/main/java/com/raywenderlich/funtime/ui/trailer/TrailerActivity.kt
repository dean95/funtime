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

package com.raywenderlich.funtime.ui.trailer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.exoplayer2.ui.PlayerView
import com.raywenderlich.funtime.R
import com.raywenderlich.funtime.data.network.model.ApiTrailer

class TrailerActivity : AppCompatActivity(), TrailerContract.View {

  companion object {
    const val MOVIE_ID_EXTRA = "movie_id_extra"
    const val ERROR_ID = -1
  }

  private lateinit var trailerView: PlayerView
  private lateinit var presenter: TrailerContract.Presenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_trailer)
    init()
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.releasePlayer()
    presenter.setMediaSessionState(false)
    presenter.deactivate()
  }

  override fun trailerFetchedSuccessfully(trailer: ApiTrailer) {
    presenter.playTrailer(trailer.url)
  }

  override fun trailerFetchFailed(throwable: Throwable) {
    Toast.makeText(this, getString(R.string.trailer_error_message), Toast.LENGTH_SHORT).show()
  }

  private fun init() {
    trailerView = findViewById(R.id.ep_trailer_view)
    presenter = TrailerPresenter(this)

    val id = intent.getIntExtra(MOVIE_ID_EXTRA, ERROR_ID)

    presenter.getTrailer(id)
    initializePlayer()
  }

  private fun initializePlayer() {
    trailerView.player = presenter.getPlayer().getPlayerImpl(this)
  }
}
