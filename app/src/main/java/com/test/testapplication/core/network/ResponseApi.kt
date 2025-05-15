package com.test.testapplication.core.network


class ResponseApi
/**
 * Instantiates a new Response api.
 *
 * @param status        the status
 * @param data          the data
 * @param apiTypeStatus the api type status
 */(
    /**
     * The Status.
     */
    val status: Status,
    /**
     * The Data.
     */
    val data: Any?,
    /**
     * The Api type status.
     */
    val apiTypeStatus: Status?
) {

    companion object {

        /**
         * Success response api.
         *
         * @param data          the data
         * @param apiTypeStatus the api type status
         * @return the response api
         */
        @JvmStatic
        fun success(data: Any, apiTypeStatus: Status?): ResponseApi {
            return ResponseApi(Status.SUCCESS, data, apiTypeStatus)
        }

        /**
         * Auth fail response api.
         *
         * @param apiTypestatus the api typestatus
         * @return the response api
         */
        @JvmStatic
        fun authFail(apiTypestatus: Status?): ResponseApi {
            return ResponseApi(Status.AUTH_FAIL, null, apiTypestatus)
        }

        /**
         * Fail response api.
         *
         * @param data          the data
         * @param apiTypeStatus the api type status
         * @return the response api
         */
        @JvmStatic
        fun fail(data: Any?, apiTypeStatus: Status?): ResponseApi {
            return ResponseApi(Status.FAIL, data, apiTypeStatus)
        }


    }

}