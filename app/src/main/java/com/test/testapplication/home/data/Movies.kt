package com.test.testapplication.home.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movies(
    @SerializedName("Search") var Search: ArrayList<Search> = arrayListOf(),
    @SerializedName("totalResults") var totalResults: String? = null,
    @SerializedName("Response") var Response: String? = null
):Parcelable

@Parcelize
data class Search(

    @SerializedName("Title") var Title: String? = null,
    @SerializedName("Year") var Year: String? = null,
    @SerializedName("imdbID") var imdbID: String? = null,
    @SerializedName("Type") var Type: String? = null,
    @SerializedName("Poster") var Poster: String? = null

):Parcelable