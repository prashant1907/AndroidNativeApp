package com.test.testapplication.home.presentation.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.testapplication.R
import com.test.testapplication.core.application.MyApplication
import com.test.testapplication.core.network.ResponseApi
import com.test.testapplication.core.network.Status
import com.test.testapplication.home.data.MovieDetail
import com.test.testapplication.home.domain.HomeRemoteUsecase
import com.tsb.app.core.common.interactors.MessageNotifyStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homeRemoteUsecase: HomeRemoteUsecase,
) : ViewModel() {

    var serviceLiveData = MutableLiveData<ResponseApi>()
    var notifyLiveData = MutableLiveData<MessageNotifyStatus>()


    var compositeDisposable = CompositeDisposable()


    var movieName = MutableLiveData("")
    var stars = MutableLiveData("")
    var duration = MutableLiveData("")
    var language = MutableLiveData("")
    var movieDescription = MutableLiveData("")
    var director = MutableLiveData("")
    var writer = MutableLiveData("")
    var actor = MutableLiveData("")
    var dataVisibility = MutableLiveData(View.GONE)


    fun getdataInternetCheck(searchKey: String, apikey: String, showShimmer: Boolean) {
        val disposable: Disposable =
            homeRemoteUsecase.isAvailInternet().subscribe { hasInternet ->
                if (hasInternet) {
                    getdata(searchKey, apikey, showShimmer)
                } else {
                    serviceLiveData.value = ResponseApi.success("", Status.NO_INTERNET_CONNECTION)
                }
            }
        try {
            compositeDisposable.add(disposable)
        } catch (e: Exception) {
            //do nothing
        }
    }

    private fun getdata(searchKey: String, apikey: String, showShimmer: Boolean) {
        compositeDisposable.add(homeRemoteUsecase.getdata(searchKey, apikey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                if (showShimmer) {
                    notifyLiveData.value = MessageNotifyStatus(Status.SHOW_PROGRESS, true)

                }
            }
            .subscribe({ responseApi ->
                if (responseApi != null) {
                    notifyLiveData.value = MessageNotifyStatus(Status.SHOW_PROGRESS, false)
                    checkResponseApiStatus(responseApi)
                }

            }) {
                notifyLiveData.value = MessageNotifyStatus(Status.SHOW_PROGRESS, false)

            }
        )


    }


    fun getMovieDetailInternetCheck(imdbId: String, apikey: String) {
        val disposable: Disposable =
            homeRemoteUsecase.isAvailInternet().subscribe { hasInternet ->
                if (hasInternet) {
                    getMovieDetail(imdbId, apikey)
                } else {
                    serviceLiveData.value = ResponseApi.success("", Status.NO_INTERNET_CONNECTION)
                }
            }
        try {
            compositeDisposable.add(disposable)
        } catch (e: Exception) {
            //do nothing
        }
    }

    private fun getMovieDetail(imdbId: String, apikey: String) {
        compositeDisposable.add(homeRemoteUsecase.getMovieDetail(imdbId, apikey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                notifyLiveData.value = MessageNotifyStatus(Status.SHOW_PROGRESS, true)

            }
            .subscribe({ responseApi ->
                if (responseApi != null) {
                    notifyLiveData.value = MessageNotifyStatus(Status.SHOW_PROGRESS, false)
                    checkResponseApiStatus(responseApi)
                }

            }) {
                notifyLiveData.value = MessageNotifyStatus(Status.SHOW_PROGRESS, false)

            }
        )


    }


    private fun checkResponseApiStatus(responseApi: ResponseApi) {
        when (responseApi.status) {
            Status.FAIL_400 -> {
                serviceLiveData.value =
                    ResponseApi.fail(responseApi.data, responseApi.apiTypeStatus)
            }
            Status.SUCCESS -> {
                serviceLiveData.value = responseApi.data?.let {
                    ResponseApi.success(
                        it,
                        responseApi.apiTypeStatus
                    )
                }
            }
            Status.AUTH_FAIL -> {
                // do nothing
            }
            else -> sendDefaultError()
        }
    }


    private fun sendDefaultError() {
        serviceLiveData.value =
            ResponseApi.fail(
                MyApplication.mainAppContext.getString(R.string.default_error),
                Status.DEFAULT_MSG
            )
    }


    fun setMovieData(data: MovieDetail) {
        dataVisibility.value = View.VISIBLE
        movieName.value = data.Title
        stars.value = data.imdbRating
        duration.value = data.Runtime
        language.value = data.Language
        movieDescription.value = data.Plot
        director.value = data.Director
        actor.value = data.Actors
        writer.value = data.Writer
    }

}