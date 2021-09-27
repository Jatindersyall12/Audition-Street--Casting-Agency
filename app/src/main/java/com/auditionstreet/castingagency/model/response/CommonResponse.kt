package com.auditionstreet.castingagency.model.response

import com.google.gson.annotations.SerializedName

data class CommonResponse(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("data")
    val data: ArrayList<String>,
    @SerializedName("msg")
    val msg: String?
)