package com.test.testapplication.home.data

import com.test.testapplication.core.UrlConstants
import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeRemoteApi {
    @GET(UrlConstants.GET_DATA)
    fun getData(@Query("s") searchKey: String,@Query("apikey") apikey : String): Single<Response<JsonObject?>>

    @GET(UrlConstants.GET_DETAIL)
    fun getMovieDetail(@Query("i") imdbId: String,@Query("apikey") apikey : String): Single<Response<JsonObject?>>
}