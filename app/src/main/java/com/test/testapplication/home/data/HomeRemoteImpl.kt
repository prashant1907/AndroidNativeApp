package com.test.testapplication.home.data

import com.test.testapplication.home.domain.HomeRepo
import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

class HomeRemoteImpl @Inject constructor(private val api: HomeRemoteApi ): HomeRepo {
    override fun getdata(searchKey : String,apikey : String): Single<Response<JsonObject?>> {
         return  api.getData(searchKey,apikey)
    }

    override fun getMovieDetail(imdbId: String, apikey: String): Single<Response<JsonObject?>> {
        return  api.getMovieDetail(imdbId,apikey)
    }
}