package com.silo.model.response
import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("message")
    var message: String,
    @SerializedName("statusCode")
    var statusCode: Int,
    @SerializedName("type")
    var type: String,
    @SerializedName("user")
    var user: Any
)