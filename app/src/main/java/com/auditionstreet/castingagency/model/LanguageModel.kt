package com.auditionstreet.castingagency.model

import com.google.gson.annotations.SerializedName

class LanguageModel {
    @SerializedName("isChecked")
    var isChecked: Boolean = false

    @SerializedName("language")
    var language: String = ""
}