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
import com.raywenderlich.funtime.data.network.model.ApiVideo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

import kotlinx.android.synthetic.main.video_item_view.view.*
import java.util.concurrent.TimeUnit

class MainAdapter : RecyclerView.Adapter<MainAdapter.VideoViewHolder>() {

  companion object {
    const val CLICK_THROTTLE_WINDOW_MILLIS = 300L
  }

  private val onVideoClickSubject: Subject<ApiVideo> = BehaviorSubject.create()

  private var videos: List<ApiVideo> = ArrayList()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
    val itemView = LayoutInflater.from(parent.context)
        .inflate(R.layout.video_item_view, parent, false)
    return VideoViewHolder(itemView, onVideoClickSubject)
  }

  override fun onBindViewHolder(holder: VideoViewHolder, position: Int) = holder.setVideo(videos[position])

  override fun getItemCount() = videos.size

  fun onVideosUpdate(videos: List<ApiVideo>) {
    this.videos = videos
    notifyDataSetChanged()
  }

  fun onItemClick() = onVideoClickSubject.throttleFirst(CLICK_THROTTLE_WINDOW_MILLIS, TimeUnit.MILLISECONDS)

  class VideoViewHolder(val view: View,
                        private val clickSubject: Subject<ApiVideo>) : RecyclerView.ViewHolder(view) {

    private lateinit var video: ApiVideo

    fun setVideo(video: ApiVideo) {
      this.video = video
      itemView.tv_main_video_title.text = video.publicId
      itemView.main_video_item_container.setOnClickListener { onMovieClick() }
    }

    private fun onMovieClick() = clickSubject.onNext(video)
  }
}