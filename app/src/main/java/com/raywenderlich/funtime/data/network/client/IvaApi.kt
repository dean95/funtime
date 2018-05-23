package com.raywenderlich.funtime.data.network.client

import com.raywenderlich.funtime.data.network.model.ApiMoviesResult
import com.raywenderlich.funtime.data.network.model.ApiTrailer
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface IvaApi {

    @Headers("Accept: application/json")
    @GET("Movies/All?Take=10&subscription-Key=f3ad3d212b344ec188a08e3d1a527ab0")
    fun getMovies(): Single<ApiMoviesResult>

    @Headers("Accept: application/json")
    @GET("Videos/GetVideo/{movieId}?Format=mp4&Expires=2018-08-21T19%3A54%3A01.304Z&subscription-Key=f3ad3d212b344ec188a08e3d1a527ab0")
    fun getTrailer(@Path("movieId")
                   movieId: Int): Single<ApiTrailer>
}