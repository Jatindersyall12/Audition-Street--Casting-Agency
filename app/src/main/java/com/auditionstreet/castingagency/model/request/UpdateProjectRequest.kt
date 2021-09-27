package com.silo.model.request

import com.google.gson.annotations.SerializedName

class UpdateProjectRequest {
   /* @SerializedName("admins")
    lateinit var admins: List<String>*/

    @SerializedName("age")
    var age: String = ""

    @SerializedName("bodyType")
    var bodyType: String = ""

    @SerializedName("projectId")
    var projectId: String = ""

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

    @SerializedName("heightFt")
    var heightFt: String = ""

    @SerializedName("heightIn")
    var heightIn: String = ""

    @SerializedName("lang")
    var lang: String = ""

    @SerializedName("location")
    var location: String = ""

    @SerializedName("title")
    var title: String = ""

    @SerializedName("toDate")
    var toDate: String = ""
}