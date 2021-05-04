package com.auditionstreet.castingagency.model.response

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class AllAdminResponse(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("msg")
    val msg: String?
) {
    data class Data(
        @SerializedName("bio")
        val bio: String?,
        @SerializedName("created_at")
        val createdAt: String?,
        @SerializedName("email")
        val email: String?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("is_checked")
        var is_checked: Boolean,
        @SerializedName("image")
        val image: String?,
        @SerializedName("logo")
        val logo: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("password")
        val password: String?,
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