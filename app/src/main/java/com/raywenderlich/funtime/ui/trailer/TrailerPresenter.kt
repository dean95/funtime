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

import com.raywenderlich.funtime.data.network.MovieService
import com.raywenderlich.funtime.device.player.MediaPlayerImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

class TrailerPresenter(trailerView: TrailerContract.View) : TrailerContract.Presenter {

  private val view = WeakReference(trailerView)
  private val disposables = CompositeDisposable()
  private val mediaPlayer = MediaPlayerImpl()

  override fun getTrailer(id: Int) {
    disposables.add(MovieService.getTrailer(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ view.get()?.trailerFetchedSuccessfully(it) },
            { view.get()?.trailerFetchFailed(it) }))
  }

  override fun getPlayer() = mediaPlayer

  override fun releasePlayer() {
    mediaPlayer.releasePlayer()
  }

  override fun playTrailer(url: String) {
    mediaPlayer.play(url)
  }

  override fun setMediaSessionState(isActive: Boolean) {
    mediaPlayer.setMediaSessionState(isActive)
  }

  override fun deactivate() {
    disposables.clear()
  }
}