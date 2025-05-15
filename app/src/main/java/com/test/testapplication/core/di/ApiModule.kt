package com.test.testapplication.core.di

import com.test.testapplication.home.data.HomeRemoteApi
import com.test.testapplication.home.data.HomeRemoteImpl
import com.test.testapplication.home.domain.HomeRepo
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun providesapiRemoteService(retrofit: Retrofit): HomeRemoteApi {
        return retrofit.create(HomeRemoteApi::class.java)
    }


    @Provides
    @Singleton
    fun providesRepo(api: HomeRemoteApi): HomeRepo {
        return HomeRemoteImpl(api)
    }

}