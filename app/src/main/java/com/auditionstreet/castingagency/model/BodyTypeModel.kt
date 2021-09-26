package com.auditionstreet.castingagency.model

import com.google.gson.annotations.SerializedName

class BodyTypeModel {
    @SerializedName("isChecked")
    var isChecked: Boolean = false

    @SerializedName("bodyType")
    var bodyType: String = ""
}