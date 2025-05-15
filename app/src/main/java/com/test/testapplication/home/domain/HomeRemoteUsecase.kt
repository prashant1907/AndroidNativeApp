package com.test.testapplication.home.domain

import com.test.testapplication.R
import com.test.testapplication.core.AppConstants
import com.test.testapplication.core.application.MyApplication
import com.test.testapplication.core.network.NetworkUseCase
import com.test.testapplication.core.network.NetworkUtil
import com.test.testapplication.core.network.ResponseApi
import com.test.testapplication.core.network.Status
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.test.testapplication.home.data.MovieDetail
import com.test.testapplication.home.data.Movies
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject


class HomeRemoteUsecase @Inject constructor(private val repo: HomeRepo) : NetworkUseCase() {


    companion object {
        @JvmStatic
        val gson = Gson()
    }

    override fun isAvailInternet(): Single<Boolean> {
        return NetworkUtil.hasInternetConnection()
    }

    override fun response200(response: Response<JsonObject?>, status: Status): ResponseApi? {
        when (status) {
            Status.DATA -> {
                val res =
                    gson.fromJson(
                        response.body(),
                        Movies::class.java
                    )
                return ResponseApi.success(res, status)

            }
            Status.MOVIEDETAIL->{
                val res =
                    gson.fromJson(
                        response.body(),
                        MovieDetail::class.java
                    )
                return ResponseApi.success(res, status)
            }


            else -> {
                return null
            }
        }

    }


    override fun response401(apiTypestatus: Status?): ResponseApi? {
        return ResponseApi.authFail(apiTypestatus)
    }

    override fun responseFail400(response: Response<JsonObject>, status: Status): ResponseApi? {
        return ResponseApi.fail(
            MyApplication.mainAppContext.getString(R.string.default_error),
            status
        )
    }

    override fun responseFail(status: Status): ResponseApi? {
        return ResponseApi.fail(
            MyApplication.mainAppContext.getString(R.string.default_error),
            status
        )
    }

    fun getdata(searchKey : String, apikey :String): Single<ResponseApi?> {
        return repo.getdata(searchKey,apikey).map { response ->
            if (response != null) {
                handleResponse(response, Status.DATA, null)
            } else {
                responseFail(Status.DATA)
            }
        }
    }

    fun getMovieDetail(imdbId : String, apikey :String): Single<ResponseApi?> {
        return repo.getMovieDetail(imdbId,apikey).map { response ->
            if (response != null) {
                handleResponse(response, Status.MOVIEDETAIL, null)
            } else {
                responseFail(Status.MOVIEDETAIL)
            }
        }
    }

    /**
     * This method is a common handling of all cases of response of network call.
     * [RES_200 ][Status]
     * This will return the ResponseApi object.
     *
     *
     * [RES_401 ][Status]
     * This will return the ResponseApi object.
     *
     *
     * [RES_400 ][Status]
     * This will return the ResponseApi object.
     *
     *
     * [RES_FAIL ][Status]
     * This will return the ResponseApi object.
     */
    private fun handleResponse(
        response: Response<JsonObject?>,
        apiTypeStatus: Status,
        errorResponse: Response<JsonObject>?
    ): ResponseApi? {
        return when (response.code()) {
            AppConstants.RES_200 -> response200(response, apiTypeStatus)
            AppConstants.RES_401 -> response401(apiTypeStatus)
            AppConstants.RES_400 -> errorResponse?.let { responseFail400(it, apiTypeStatus) }
            AppConstants.RES_FAIL -> responseFail(apiTypeStatus)
            else -> responseFail(apiTypeStatus)
        }
    }

}