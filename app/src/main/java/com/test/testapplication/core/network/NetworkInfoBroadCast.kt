package com.test.testapplication.core.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class NetworkInfoBroadCast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = (activeNetwork != null
                && activeNetwork.isConnected)
        connectivityCallback?.onNetworkConnectionChanged(isConnected)
    }

    companion object {
        private val connectivityCallback: com.test.testapplication.core.network.ConnectivityCallback? = null
    }
}