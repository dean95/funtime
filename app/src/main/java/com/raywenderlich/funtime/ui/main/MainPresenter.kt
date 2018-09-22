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

import android.app.Activity
import android.content.Intent
import com.raywenderlich.funtime.data.network.VideosService
import com.raywenderlich.funtime.data.network.model.ApiResponse
import com.raywenderlich.funtime.ui.video.VideoViewActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

class MainPresenter(view: MainContract.View) : MainContract.Presenter {

  private val view = WeakReference<MainContract.View>(view)
  private val disposables = CompositeDisposable()

  override fun fetchSampleVideos() {
    disposables.add(
        VideosService.fetchVideos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { apiResponse -> onVideosFetchedSuccessfully(apiResponse) },
                { throwable -> onVideosFetchError(throwable) }
            ))
  }

  override fun showVideoScreen(videoUrl: String) {
    val intent = Intent((view.get() as Activity), VideoViewActivity::class.java)
    intent.putExtra(VideoViewActivity.VIDEO_URL_EXTRA, videoUrl)
    (view.get() as Activity).startActivity(intent)
  }

  override fun deactivate() {
    disposables.clear()
  }

  private fun onVideosFetchedSuccessfully(videoData: ApiResponse?) {
    view.get()?.renderVideos(videoData?.resources!!)
  }

  private fun onVideosFetchError(throwable: Throwable) {
    view.get()?.showErrorMessage()
  }
}