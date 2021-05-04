package com.silo.model.response
import com.google.gson.annotations.SerializedName


data class LoginResponse(
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