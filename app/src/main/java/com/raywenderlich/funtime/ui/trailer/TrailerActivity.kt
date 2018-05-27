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

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.raywenderlich.funtime.R
import com.raywenderlich.funtime.data.network.MovieService
import com.raywenderlich.funtime.data.network.model.ApiTrailer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TrailerActivity : AppCompatActivity(), Player.EventListener {

    companion object {
        const val MOVIE_ID_EXTRA = "movie_id_extra"
        const val ERROR_ID = -1
        val TAG = TrailerActivity::class.java.simpleName

        //TODO Just for testing
        const val TRAILER_URL =
                "https://video.internetvideoarchive.net/video.mp4?cmd=6&fmt=4&customerid=222333&publishedid=2&rnd=29&e=1534881241&maxrate=600000&h=805c93e76f9b06e10a0ab990e3dd6b4d"
    }

    private lateinit var trailerView: PlayerView
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var stateBuilder: PlaybackStateCompat.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trailer)

        val id = intent.getIntExtra(MOVIE_ID_EXTRA, ERROR_ID)

        trailerView = findViewById(R.id.ep_trailer_view)

        //TODO Don't send actual request yet.
//        getTrailer(id)
        initializePlayer()

        initializeMediaSession()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
        mediaSession.isActive = false
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
    }

    override fun onSeekProcessed() {
    }

    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
    }

    override fun onLoadingChanged(isLoading: Boolean) {
    }

    override fun onPositionDiscontinuity(reason: Int) {
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if ((playbackState == Player.STATE_READY) && playWhenReady) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    exoPlayer.currentPosition, 1f)
        } else if ((playbackState == Player.STATE_READY)) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    exoPlayer.currentPosition, 1f)
        }
    }

    private fun getTrailer(movieId: Int) {
        MovieService.getTrailer(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::trailerFetchedSuccessfully,
                        this::trailerFetchFailed)
    }

    private fun initializeMediaSession() {
        mediaSession = MediaSessionCompat(this, TAG)

        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        )

        mediaSession.setMediaButtonReceiver(null)

        stateBuilder = PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY or
                                PlaybackStateCompat.ACTION_PAUSE or
                                PlaybackStateCompat.ACTION_PLAY_PAUSE or
                                PlaybackStateCompat.ACTION_FAST_FORWARD or
                                PlaybackStateCompat.ACTION_REWIND
                )

        mediaSession.setPlaybackState(stateBuilder.build())

        mediaSession.setCallback(SessionCallback())

        mediaSession.isActive = true
    }

    private fun initializePlayer() {
        //Create an instance of the player
        val trackSelector = DefaultTrackSelector()
        val loadControl = DefaultLoadControl()
        val renderersFactory = DefaultRenderersFactory(this)

        exoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl)
        exoPlayer.addListener(this)

        trailerView.player = exoPlayer

        //Prepare media source
        val userAgent = Util.getUserAgent(this, getString(R.string.app_name))
        val mediaSource = ExtractorMediaSource.Factory(DefaultDataSourceFactory(this, userAgent))
                .setExtractorsFactory(DefaultExtractorsFactory())
                .createMediaSource(Uri.parse(TRAILER_URL))
        exoPlayer.prepare(mediaSource)

        //Play
        exoPlayer.playWhenReady = true
    }

    private fun releasePlayer() {
        exoPlayer.stop()
        exoPlayer.release()
    }

    private fun trailerFetchedSuccessfully(trailer: ApiTrailer) {
        //Prepare media source
        val userAgent = Util.getUserAgent(this, getString(R.string.app_name))
        val mediaSource = ExtractorMediaSource.Factory(DefaultDataSourceFactory(this, userAgent))
                .setExtractorsFactory(DefaultExtractorsFactory())
                .createMediaSource(Uri.parse(trailer.url))
        exoPlayer.prepare(mediaSource)

        //Play
        exoPlayer.playWhenReady = true
    }

    private fun trailerFetchFailed(throwable: Throwable) {
        Toast.makeText(this, getString(R.string.trailer_error_message), Toast.LENGTH_SHORT).show()
    }

    private inner class SessionCallback : MediaSessionCompat.Callback() {

        private val SEEK_WINDOW_MILLIS = 10000

        override fun onPlay() {
            exoPlayer.playWhenReady = true
        }

        override fun onPause() {
            exoPlayer.playWhenReady = false
        }

        override fun onRewind() {
            exoPlayer.seekTo(exoPlayer.currentPosition - SEEK_WINDOW_MILLIS)
        }

        override fun onFastForward() {
            exoPlayer.seekTo(exoPlayer.currentPosition + SEEK_WINDOW_MILLIS)
        }
    }
}
