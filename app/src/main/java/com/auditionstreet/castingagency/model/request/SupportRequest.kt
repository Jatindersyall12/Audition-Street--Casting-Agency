package com.silo.model.request
import com.google.gson.annotations.SerializedName


data class SupportRequest(
    var phoneNumber: String = "",
    var message: String = "",
    var userType: String = ""
)