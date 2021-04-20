package com.silo.model.response
import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @SerializedName("authToken")
    var authToken: String,
    @SerializedName("completeProfileStep")
    var completeProfileStep: Int,
    @SerializedName("countryCode")
    var countryCode: String,
    @SerializedName("cryptocurrencies")
    var cryptocurrencies: List<Any>,
    @SerializedName("email")
    var email: String,
    @SerializedName("_id")
    var id: String,
    @SerializedName("isEmailVerified")
    var isEmailVerified: Boolean,
    @SerializedName("isPhoneVerified")
    var isPhoneVerified: Boolean,
    @SerializedName("message")
    var message: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("phoneNo")
    var phoneNo: String,
    @SerializedName("pin_size")
    var pinSize: Any,
    @SerializedName("statusCode")
    var statusCode: Int,
    @SerializedName("type")
    var type: String,
    @SerializedName("userName")
    var userName: String
)