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
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.raywenderlich.funtime.R
import com.raywenderlich.funtime.ui.main.MainPresenter.Companion.REQUEST_TAKE_GALLERY_VIDEO

class MainActivity : AppCompatActivity(), MainContract.View {

  private lateinit var progressBar: ProgressBar
  private lateinit var videosList: ListView
  private lateinit var emptyText: TextView
  private lateinit var presenter: MainContract.Presenter
  private lateinit var videosAdapter: ArrayAdapter<String>
  private val videos = ArrayList<String>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    init()
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.deactivate()
  }

  override fun videoUploadSuccessfully(publicUrl: String) {
    hideLoadingIndicator()
    videos.add(publicUrl)
    videosAdapter.notifyDataSetChanged()
  }

  override fun videoUploadFailedFailed() {
    if (videos.size > 0) hideEmptyView() else showEmptyView()
    hideLoadingIndicator()
    Toast.makeText(this, getString(R.string.main_error_message), Toast.LENGTH_SHORT).show()
  }

  override fun videoUploadInProgress() {
    showLoadingIndicator()
    hideEmptyView()
  }

  fun onAddVideoButtonClick(view: View) {
    presenter.selectVideo()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
        val selectedVideoUri = data?.data
        presenter.uploadVideo(selectedVideoUri!!)
      }
    }
  }

  private fun init() {
    progressBar = findViewById(R.id.pb_main)
    videosList = findViewById(R.id.lv_videos)
    emptyText = findViewById(R.id.tv_empty)

    presenter = MainPresenter(this)

    videosAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, videos)
    videosList.adapter = videosAdapter
    videosList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
      onVideoItemClick(videosList.getItemAtPosition(position).toString())
    }
  }

  private fun onVideoItemClick(videoUrl: String) {
    presenter.showVideoScreen(videoUrl)
  }

  private fun showLoadingIndicator() {
    progressBar.visibility = View.VISIBLE
  }

  private fun hideLoadingIndicator() {
    progressBar.visibility = View.GONE
  }

  private fun hideEmptyView() {
    emptyText.visibility = View.GONE
  }

  private fun showEmptyView() {
    emptyText.visibility = View.VISIBLE
  }
}
