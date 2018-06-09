package com.raywenderlich.funtime.ui.trailer

import com.raywenderlich.funtime.data.network.MovieService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

class TrailerPresenter(trailerView: TrailerContract.View) : TrailerContract.Presenter {

  private val view = WeakReference(trailerView)
  private val disposables = CompositeDisposable()

  override fun getTrailer(id: Int) {
    disposables.add(MovieService.getTrailer(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ view.get()?.trailerFetchedSuccessfully(it) },
            { view.get()?.trailerFetchFailed(it) }))
  }
}