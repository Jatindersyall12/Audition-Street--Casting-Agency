package com.auditionstreet.castingagency.model.response

import com.google.gson.annotations.SerializedName

 data class OtherProfileResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("msg")
    val msg: String
) {
    data class Data(
        @SerializedName("acceptedApplication")
        val acceptedApplication: Int,
        @SerializedName("artistDetails")
        val artistDetails: ArtistDetails,
        @SerializedName("media")
        val media: List<Media>,
        @SerializedName("totalApplication")
        val totalApplication: Int
    ) {
        data class ArtistDetails(
            @SerializedName("age")
            val age: String,
            @SerializedName("bio")
            val bio: String,
            @SerializedName("bodyType")
            val bodyType: List<Any>,
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("deviceToken")
            val deviceToken: String,
            @SerializedName("email")
            val email: String,
            @SerializedName("gender")
            val gender: String,
            @SerializedName("heightFt")
            val heightFt: String,
            @SerializedName("heightIn")
            val heightIn: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("image")
            val image: String,
            @SerializedName("language")
            val language: List<Any>,
            @SerializedName("name")
            val name: String,
            @SerializedName("phoneNumber")
            val phoneNumber: String,
            @SerializedName("planPurchase")
            val planPurchase: String,
            @SerializedName("skinTone")
            val skinTone: List<Any>,
            @SerializedName("socialId")
            val socialId: String,
            @SerializedName("socialType")
            val socialType: String,
            @SerializedName("trialRequest")
            val trialRequest: String,
            @SerializedName("updated_at")
            val updatedAt: String,
            @SerializedName("video")
            val video: String,
            @SerializedName("year")
            val year: String
        )

        data class Media(
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("mediaType")
            val mediaType: String,
            @SerializedName("mediaUrl")
            val mediaUrl: String,
            @SerializedName("updated_at")
            val updatedAt: String,
            @SerializedName("userId")
            val userId: String,
            @SerializedName("userType")
            val userType: String
        )
    }
}