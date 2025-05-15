package com.test.testapplication.core.network

import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.Response
abstract class NetworkUseCase {
    /**
     * Is avail internet single.
     *
     * @return the single
     */
    abstract fun isAvailInternet(): Single<Boolean>

    /**
     * Response 200 response api.
     *
     * @param response the response
     * @param status   the status
     * @return the response api
     */
    abstract fun response200(
        response: Response<JsonObject?>,
        status: Status
    ): ResponseApi?

    /**
     * Response 401 response api.
     *
     * @param apiTypestatus the api typestatus
     * @return the response api
     */
    abstract fun response401(apiTypestatus: Status?): ResponseApi?

    /**
     * Response fail 400 response api.
     *
     * @param response the response
     * @param status   the status
     * @return the response api
     */
    abstract fun responseFail400(response: Response<JsonObject>,
        status: Status
    ): ResponseApi?

    /**
     * Response fail response api.
     *
     * @param status the status
     * @return the response api
     */
    abstract fun responseFail(status: Status): ResponseApi?
}