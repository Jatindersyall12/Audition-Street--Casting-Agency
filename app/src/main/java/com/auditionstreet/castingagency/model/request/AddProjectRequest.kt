package com.silo.model.request
import com.google.gson.annotations.SerializedName
data class AddProjectRequest(
    @SerializedName("admins")
    val admins: List<Int?>?,
    @SerializedName("age")
    val age: String?,
    @SerializedName("bodyType")
    val bodyType: String?,
    @SerializedName("castingId")
    val castingId: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("exp")
    val exp: String?,
    @SerializedName("fromDate")
    val fromDate: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("height")
    val height: String?,
    @SerializedName("lang")
    val lang: String?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("toDate")
    val toDate: String?
)