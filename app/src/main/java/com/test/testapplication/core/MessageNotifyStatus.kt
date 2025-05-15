package com.tsb.app.core.common.interactors

import com.test.testapplication.core.network.Status

class MessageNotifyStatus(status: Status?, data: Any?) {
    /**
     * Gets status.
     *
     * @return the status
     */
    var status: Status? = null
    /**
     * Gets data.
     *
     * @return the data
     */
    var data: Any? = null

    /**
     * Instantiates a new Messge notify status.
     *
     * @param status the status
     * @param data   the data
     */
    init {
        this.status = status
        this.data = data
    }
}