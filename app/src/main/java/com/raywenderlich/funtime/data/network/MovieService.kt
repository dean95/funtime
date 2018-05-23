package com.raywenderlich.funtime.data.network

import com.raywenderlich.funtime.data.network.client.IvaApi
import com.raywenderlich.funtime.data.network.model.ApiMoviesResult
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MovieService {

    private val api: IvaApi

    companion object {
        const val BASE_URL = "https://ee.iva-api.com/"
    }

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        api = retrofit.create(IvaApi::class.java)
    }

    fun getMovies() = api.getMovies()
}