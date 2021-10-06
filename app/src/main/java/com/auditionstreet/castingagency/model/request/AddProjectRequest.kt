package com.silo.model.request

import com.google.gson.annotations.SerializedName

class AddProjectRequest {
    @SerializedName("admins")
    lateinit var admins: List<String>

    @SerializedName("age")
    var age: String = ""

    @SerializedName("bodyType")
    lateinit var bodyType: ArrayList<Int>

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
    lateinit var lang: ArrayList<Int>/* = ""*/

    @SerializedName("skinTone")
    lateinit var skinTone: ArrayList<Int>

    @SerializedName("location")
    var location: String = ""

    @SerializedName("title")
    var title: String = ""

    @SerializedName("toDate")
    var toDate: String = ""
}