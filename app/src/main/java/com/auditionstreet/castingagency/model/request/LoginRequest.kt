package com.silo.model.request
import com.google.gson.annotations.SerializedName


data class LoginRequest(
    var email: String = "",
    var password: String = ""
)