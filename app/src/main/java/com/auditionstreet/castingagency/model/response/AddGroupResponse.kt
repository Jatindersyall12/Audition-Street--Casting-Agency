package com.auditionstreet.castingagency.model.response

import com.google.gson.annotations.SerializedName

data class AddGroupResponse(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("data")
    val `data`: List<Any?>,
    @SerializedName("msg")
    val msg: String
)