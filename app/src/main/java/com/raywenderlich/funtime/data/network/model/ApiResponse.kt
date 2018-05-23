package com.raywenderlich.funtime.data.network.model

import com.google.gson.annotations.SerializedName

data class ApiMoviesResult(@SerializedName("TotalCount") val totalCount: Int,
                           @SerializedName("Result") val items: List<ApiMovie>)

data class ApiMovie(@SerializedName("Id") val id: Int,
                    @SerializedName("Title") val title: String,
                    @SerializedName("Year") val year: Int)

data class ApiTrailer(@SerializedName("Url") val url: String)