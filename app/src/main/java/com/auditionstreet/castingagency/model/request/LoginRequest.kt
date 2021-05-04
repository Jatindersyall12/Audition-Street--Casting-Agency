package com.silo.model.request


data class LoginRequest(
    var email: String = "",
    var password: String = "",
    var isSocial: String = "",
    var name: String = "",
    var socialType: String = "",
    var socialId: String = ""

)