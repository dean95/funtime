package com.raywenderlich.funtime.ui.trailer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
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
                "https://dlza6g8e6iucb.cloudfront.net/0342-005011/005011_24.mp4?c=222333&max_bitrate=600000&sub=not-set&ser=635992566310000000&Expires=1527102639&Signature=FkpzVAAuuF63mQa03kVyJrY64sB5ARfcu5DuAPpS7FvC1PWj-zIPX1l92MODZORCOIKBdmu8GZidE487wZnORNQY9JErNddB24Gx6WfeZibPrEYnPDZkE87w7KmdH7OIAhT1FrNU~uvlu-hh4Nct3afgLd7ARdObp2Ch5gdy6AVEWDHFpuhrmOW4pNjb9HKVEHmpC4WbZRi4TPJmX1QdEd6Qzk1XM44lI4gvOYn8woWYkrZhMESKepE2203f0rUQOBW1gquVeVofBDwfYB6T-MMum4gLgl2xBGIIbKMb1iBI3KFALvYuuc7ynXDINYRPkox2LpTul3vVgfOI2VV8VQ__&Key-Pair-Id=APKAIUDDGLY3RASDQSZQ"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trailer)

        val id = intent.getIntExtra(MOVIE_ID_EXTRA, ERROR_ID)

//        getTrailer(id)
    }

    private fun getTrailer(movieId: Int) {
        MovieService.getTrailer(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::trailerFetchedSuccessfully,
                        this::trailerFetchFailed)
    }

    private fun trailerFetchedSuccessfully(trailer: ApiTrailer) {
        Log.d(TrailerActivity::class.java.simpleName, trailer.url)
    }

    private fun trailerFetchFailed(throwable: Throwable) {
        throwable.printStackTrace()
    }
}
