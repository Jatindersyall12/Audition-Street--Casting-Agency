package com.auditionstreet.castingagency.model.response

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class ProjectResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    var `data`: ArrayList<Data>,
    @SerializedName("message")
    val message: String
) {
    data class Data(
        @SerializedName("title")
        val title: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("projectId")
        val projectId: String,
        @SerializedName("artistId")
        val artistId: String,
        @SerializedName("castingId")
        val castingId: String,
        @SerializedName("artistStatus")
        val artistStatus: String,
        @SerializedName("castingStatus")
        val castingStatus: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("artistName")
        val artistName: String,
        @SerializedName("heightFt")
        val heightFt: String,
        @SerializedName("heightIn")
        val heightIn: String,
        @SerializedName("age")
        val age: String,
        @SerializedName("gender")
        val gender: String,
        @SerializedName("image")
        val image: String,
        @SerializedName("video")
        val video: String
    )
}