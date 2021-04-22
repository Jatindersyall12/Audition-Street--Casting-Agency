package com.silo.model.request
import com.google.gson.annotations.SerializedName


data class ProjectRequest(
    var email: String = "",
    var password: String = ""
)