package com.raywenderlich.funtime.ui.trailer

import com.raywenderlich.funtime.data.network.model.ApiTrailer

interface TrailerContract {

  interface Presenter {

    fun getTrailer(id: Int)
  }

  interface View {

    fun trailerFetchedSuccessfully(trailer: ApiTrailer)

    fun trailerFetchFailed(throwable: Throwable)
  }
}