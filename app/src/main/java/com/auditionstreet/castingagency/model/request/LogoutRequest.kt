package com.silo.model.request
import com.google.gson.annotations.SerializedName


data class LogoutRequest(
    var userId: String = "",
    var userType: String = ""
)