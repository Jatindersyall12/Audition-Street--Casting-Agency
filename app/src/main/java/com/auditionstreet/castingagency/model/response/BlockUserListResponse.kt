package com.auditionstreet.castingagency.model.response
import com.google.gson.annotations.SerializedName


data class BlockUserListResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: ArrayList<Data>,
    @SerializedName("msg")
    val msg: String
) {
    data class Data(
        @SerializedName("age")
        val age: String,
        @SerializedName("artistId")
        val artistId: String,
        @SerializedName("artistImage")
        val artistImage: String,
        @SerializedName("artistName")
        val artistName: String,
        @SerializedName("castingId")
        val castingId: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("gender")
        val gender: String,
        @SerializedName("heightFt")
        val heightFt: String,
        @SerializedName("heightIn")
        val heightIn: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("status")
        val status: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("video")
        val video: String
    )
}