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

package com.raywenderlich.funtime.ui.video

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.exoplayer2.ui.PlayerView
import com.raywenderlich.funtime.R

class VideoViewActivity : AppCompatActivity(), VideoViewContract.View {

  companion object {
    const val VIDEO_URL_EXTRA = "video_url_extra"
  }

  private lateinit var videoView: PlayerView
  private lateinit var presenter: VideoViewContract.Presenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_video_view)
    init()
  }

  override fun onPause() {
    super.onPause()
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
      presenter.releasePlayer()
    }
  }

  override fun onStop() {
    super.onStop()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      presenter.releasePlayer()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.setMediaSessionState(false)
    presenter.deactivate()
  }

  private fun init() {
    videoView = findViewById(R.id.ep_video_view)
    presenter = VideoViewPresenter(this)

    initializePlayer()

    val videoUrl = intent.getStringExtra(VIDEO_URL_EXTRA)
    presenter.playVideo(videoUrl)
  }

  private fun initializePlayer() {
    videoView.player = presenter.getPlayer().getPlayerImpl(this)
  }
}
