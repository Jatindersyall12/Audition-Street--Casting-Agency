package com.auditionstreet.castingagency.model.response

import com.google.gson.annotations.SerializedName

data class UploadMediaResponse(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("msg")
    val msg: String?
) {
    data class Data(
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
}