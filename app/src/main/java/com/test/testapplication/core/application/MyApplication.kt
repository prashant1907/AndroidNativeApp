package com.test.testapplication.core.application

import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.test.testapplication.core.di.ApiModule
import com.test.testapplication.core.di.ApplicationComponent
import com.test.testapplication.core.di.DaggerApplicationComponent
import com.test.testapplication.core.network.NetworkInfoBroadCast
import javax.inject.Inject

class MyApplication : Application() {

    @Inject
    lateinit var appComponent: ApplicationComponent

    var mNetworkInfoBroadCast: NetworkInfoBroadCast? = null


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        mainAppContext = appContext.applicationContext

        mNetworkInfoBroadCast = NetworkInfoBroadCast()
        networkCheck()
        appComponent = com.test.testapplication.core.di.DaggerApplicationComponent
            .builder()
            .apiModule(ApiModule())
            .build()
        appComponent.doInject(this)
    }


    private fun networkCheck() {
        try {
            this.registerReceiver(
                mNetworkInfoBroadCast, IntentFilter(
                    ConnectivityManager.CONNECTIVITY_ACTION
                )
            )
        } catch (e: Exception) {
            //do nothing
        }
    }

    companion object {
        //this is for Resources
        lateinit var mainAppContext: Context

        //this is  for dagger
        lateinit var appContext: MyApplication

    }


}