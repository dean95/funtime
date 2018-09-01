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
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.raywenderlich.funtime.R
import com.raywenderlich.funtime.ui.video.VideoViewActivity
import java.lang.ref.WeakReference

class MainPresenter(view: MainContract.View) : MainContract.Presenter {

  companion object {
    const val REQUEST_TAKE_GALLERY_VIDEO = 1234
    const val TYPE_VIDEO = "video/*"
    const val UPLOAD_PRESET = "ig5ysybo"
    const val OPTION_NAME = "resource_type"
    const val OPTION_VALUE = "video"
    const val KEY_URL = "url"
  }

  private val view = WeakReference<MainContract.View>(view)

  override fun selectVideo() {
    val intent = Intent()
    intent.type = TYPE_VIDEO
    intent.action = Intent.ACTION_GET_CONTENT
    (view.get() as Activity).startActivityForResult(
        Intent.createChooser(intent, (view.get() as Activity).getString(R.string.main_select_video_title_text)),
        REQUEST_TAKE_GALLERY_VIDEO)
  }

  override fun uploadVideo(videoUri: Uri) {
    MediaManager.get()
        .upload(videoUri)
        .unsigned(UPLOAD_PRESET)
        .option(OPTION_NAME, OPTION_VALUE)
        .callback(object : UploadCallback {
          override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
            val publicUrl = resultData?.get(KEY_URL) as String
            view.get()?.videoUploadSuccessfully(publicUrl)
          }

          override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {

          }

          override fun onReschedule(requestId: String?, error: ErrorInfo?) {

          }

          override fun onError(requestId: String?, error: ErrorInfo?) {
            view.get()?.videoUploadFailedFailed()
          }

          override fun onStart(requestId: String?) {
            view.get()?.videoUploadInProgress()
          }
        }).dispatch()
  }

  override fun showVideoScreen(videoUrl: String) {
    val intent = Intent((view.get() as Activity), VideoViewActivity::class.java)
    intent.putExtra(VideoViewActivity.VIDEO_URL_EXTRA, videoUrl)
    (view.get() as Activity).startActivity(intent)
  }

  override fun deactivate() {
  }
}