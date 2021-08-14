package com.auditionstreet.castingagency.model.response

import com.google.gson.annotations.SerializedName

data class OtherProfileResponse(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("msg")
    val msg: String?
) {
    data class Data(
        @SerializedName("artistDetails")
        val artistDetails: ArtistDetails?,
        @SerializedName("media")
        val media: List<Media?>?
    ) {
        data class ArtistDetails(
            @SerializedName("age")
            val age: String?,
            @SerializedName("heightFt")
            val heightFt: String?,
            @SerializedName("heightIn")
            val heightIn: String?,
            @SerializedName("bio")
            val bio: String?,
            @SerializedName("bodyType")
            val bodyType: String?,
            @SerializedName("created_at")
            val createdAt: String?,
            @SerializedName("deviceToken")
            val deviceToken: String?,
            @SerializedName("email")
            val email: String?,
            @SerializedName("height")
            val height: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("image")
            val image: String?,
            @SerializedName("language")
            val language: String?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("gender")
            val gender: String?,
            @SerializedName("phoneNumber")
            val phoneNumber: String?,
            @SerializedName("skinTone")
            val skinTone: String?,
            @SerializedName("socialId")
            val socialId: String?,
            @SerializedName("socialType")
            val socialType: String?,
            @SerializedName("updated_at")
            val updatedAt: String?,
            @SerializedName("video")
            val video: String?,
            @SerializedName("year")
            val year: String?
        )

        data class Media(
            @SerializedName("created_at")
            val createdAt: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("mediaType")
            val mediaType: String?,
            @SerializedName("mediaUrl")
            val mediaUrl: String?,
            @SerializedName("updated_at")
            val updatedAt: String?,
            @SerializedName("userId")
            val userId: String?,
            @SerializedName("userType")
            val userType: String?
        )
    }
}