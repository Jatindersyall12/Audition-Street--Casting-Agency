package com.silo.model.request

import com.google.gson.annotations.SerializedName

class AddProjectRequest {
    @SerializedName("admins")
    lateinit var admins: List<String>

    @SerializedName("age")
    var age: String = ""

    @SerializedName("bodyType")
    var bodyType: String = ""

    @SerializedName("castingId")
    var castingId: String = ""

    @SerializedName("description")
    var description: String = ""

    @SerializedName("exp")
    var exp: String = ""

    @SerializedName("fromDate")
    var fromDate: String = ""

    @SerializedName("gender")
    var gender: String = ""

    @SerializedName("height")
    var height: String = ""

    @SerializedName("lang")
    var lang: String = ""

    @SerializedName("location")
    var location: String = ""

    @SerializedName("title")
    var title: String = ""

    @SerializedName("toDate")
    var toDate: String = ""
}