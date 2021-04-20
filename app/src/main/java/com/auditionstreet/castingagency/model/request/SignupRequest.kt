package com.silo.model.request
import com.google.gson.annotations.SerializedName

class SignupRequest {
    @SerializedName("countryCode")
    var countryCode: String =""

    @SerializedName("email")
    var email: String=""

    @SerializedName("name")
    var name: String=""

    @SerializedName("password")
    var password: String=""

    @SerializedName("phoneNo")
    var phoneNo: String=""

    @SerializedName("userName")
    var userName: String=""
}