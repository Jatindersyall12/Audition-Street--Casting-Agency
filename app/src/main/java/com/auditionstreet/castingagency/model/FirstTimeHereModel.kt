package com.auditionstreet.castingagency.model

import com.google.gson.annotations.SerializedName

class FirstTimeHereModel {
    @SerializedName("firstTimeImage")
    var firstTimeImage: Int = 0

    @SerializedName("firstTimeText")
    var firstTimeText: String = ""
}