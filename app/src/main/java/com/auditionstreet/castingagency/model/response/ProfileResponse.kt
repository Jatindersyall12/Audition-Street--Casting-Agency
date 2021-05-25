package com.auditionstreet.castingagency.model.response

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class ProfileResponse(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("msg")
    val msg: String?
) {
    data class Data(
        @SerializedName("castingDetails")
        val castingDetails: CastingDetails?,
        @SerializedName("media")
        val media: List<Media?>?
    ) {
        data class CastingDetails(
            @SerializedName("agencyType")
            val agencyType: String?,
            @SerializedName("bio")
            val bio: String?,
            @SerializedName("companyName")
            val companyName: String?,
            @SerializedName("created_at")
            val createdAt: String?,
            @SerializedName("email")
            val email: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("image")
            val image: String?,
            @SerializedName("logo")
            val logo: String?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("pastWork")
            val pastWork: String?,
            @SerializedName("socialId")
            val socialId: String?,
            @SerializedName("socialType")
            val socialType: String?,
            @SerializedName("updated_at")
            val updatedAt: String?,
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