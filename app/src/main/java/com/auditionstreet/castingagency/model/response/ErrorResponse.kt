package com.silo.model.response
import com.google.gson.annotations.SerializedName

class ErrorResponse {
    @SerializedName("error")
    var error: String=""

    @SerializedName("message")
    var message: String=""

    @SerializedName("statusCode")
    var statusCode: Int=0

    @SerializedName("type")
    var type: Int=0
}