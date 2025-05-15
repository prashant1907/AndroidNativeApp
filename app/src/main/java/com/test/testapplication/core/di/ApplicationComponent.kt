package com.test.testapplication.core.di

import com.test.testapplication.home.presentation.view.fragment.HomeFragment
import com.test.testapplication.home.presentation.view.fragment.MovieDetailFragment
import com.test.testapplication.core.application.MyApplication
import com.test.testapplication.core.network.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class,ApiModule::class, NetworkModule::class])
interface ApplicationComponent {
    fun doInject(myApplication: MyApplication)
    fun doInject(homeFragment: HomeFragment)
    fun doInject(movieDetailFragment: MovieDetailFragment)
}