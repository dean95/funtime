package com.raywenderlich.funtime.data.network.client

import com.raywenderlich.funtime.data.network.model.ApiMoviesResult
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers

interface IvaApi {

    @Headers("Accept: application/json")
    @GET("Movies/All?Take=10&subscription-Key=f3ad3d212b344ec188a08e3d1a527ab0")
    fun getMovies(): Single<ApiMoviesResult>
}