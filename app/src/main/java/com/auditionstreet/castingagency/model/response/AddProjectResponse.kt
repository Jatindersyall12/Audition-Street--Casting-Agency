package com.auditionstreet.castingagency.model.response

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class AddProjectResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("msg")
    val msg: String
) {
    data class Data(
        @SerializedName("age")
        val age: String,
        @SerializedName("bodyType")
        val bodyType: String,
        @SerializedName("castingId")
        val castingId: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("exp")
        val exp: String,
        @SerializedName("fromDate")
        val fromDate: String,
        @SerializedName("gender")
        val gender: String,
        @SerializedName("height")
        val height: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("lang")
        val lang: String,
        @SerializedName("location")
        val location: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("toDate")
        val toDate: String,
        @SerializedName("updated_at")
        val updatedAt: String
    )
}