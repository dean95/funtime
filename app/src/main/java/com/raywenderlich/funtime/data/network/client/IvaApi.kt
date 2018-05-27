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