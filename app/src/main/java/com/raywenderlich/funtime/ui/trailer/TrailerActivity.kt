package com.raywenderlich.funtime.ui.trailer

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.raywenderlich.funtime.R
import com.raywenderlich.funtime.data.network.MovieService
import com.raywenderlich.funtime.data.network.model.ApiTrailer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TrailerActivity : AppCompatActivity() {

    companion object {
        const val MOVIE_ID_EXTRA = "movie_id_extra"
        const val ERROR_ID = -1

        //TODO Just for testing
        const val TRAILER_URL =
                "https://video.internetvideoarchive.net/video.mp4?cmd=6&fmt=4&customerid=222333&publishedid=2&rnd=29&e=1534881241&maxrate=600000&h=805c93e76f9b06e10a0ab990e3dd6b4d"
    }

    private lateinit var trailerView: PlayerView
    private lateinit var exoPlayer: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trailer)

        val id = intent.getIntExtra(MOVIE_ID_EXTRA, ERROR_ID)

        trailerView = findViewById(R.id.ep_trailer_view)

        //TODO Don't send actual request yet.
//        getTrailer(id)
        initializePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun getTrailer(movieId: Int) {
        MovieService.getTrailer(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::trailerFetchedSuccessfully,
                        this::trailerFetchFailed)
    }

    private fun initializePlayer() {
        //Create an instance of the player
        val trackSelector = DefaultTrackSelector()
        val loadControl = DefaultLoadControl()
        val renderersFactory = DefaultRenderersFactory(this)

        exoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl)

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
}
