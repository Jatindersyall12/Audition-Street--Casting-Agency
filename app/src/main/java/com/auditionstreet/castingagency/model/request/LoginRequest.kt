package com.silo.model.request


data class LoginRequest(
    var email: String = "",
    var password: String = ""
)