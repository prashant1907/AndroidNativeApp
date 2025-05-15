package com.test.testapplication.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.test.testapplication.core.application.MyApplication
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable

object NetworkUtil {
    @JvmStatic
    fun hasInternetConnection(): Single<Boolean> {
        return Single
            .fromCallable(Callable {
                val connMgr =
                    MyApplication.mainAppContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                val network = connMgr.activeNetwork ?: return@Callable false

                val activeNetwork =
                    connMgr.getNetworkCapabilities(network) ?: return@Callable false

                return@Callable when {
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}