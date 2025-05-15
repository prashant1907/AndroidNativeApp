package com.test.testapplication.home.domain

import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.Response

interface HomeRepo {
    fun getdata(searchKey : String, apikey :String): Single<Response<JsonObject?>>
    fun getMovieDetail(imdbId : String, apikey :String): Single<Response<JsonObject?>>
}