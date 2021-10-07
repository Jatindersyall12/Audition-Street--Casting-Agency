package com.auditionstreet.castingagency.model.response

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class MyProjectDetailResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("msg")
    val msg: String
) {
    data class Data(
        @SerializedName("admins")
        val admins: ArrayList<Admin>,
        @SerializedName("projectRequests")
        val projectRequests: ArrayList<ProjectRequests>,
        @SerializedName("projectDetails")
        val projectDetails: ProjectDetails
    ) {
        data class Admin(
            @SerializedName("bio")
            val bio: String,
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("email")
            val email: String,
            @SerializedName("id")
            val id: String,
            @SerializedName("image")
            val image: String,
            @SerializedName("logo")
            val logo: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("password")
            val password: String,
            @SerializedName("pastWork")
            val pastWork: String,
            @SerializedName("socialId")
            val socialId: String,
            @SerializedName("socialType")
            val socialType: String,
            @SerializedName("updated_at")
            val updatedAt: String,
            @SerializedName("year")
            val year: String
        )

        data class ProjectRequests(
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
            @SerializedName("artistName")
            val artistName: String,
            @SerializedName("artistImage")
            val artistImage: String,
            @SerializedName("heightFt")
            val heightFt: String,
            @SerializedName("heightIn")
            val heightIn: String,
            @SerializedName("age")
            val age: String,
            @SerializedName("gender")
            val gender: String,
            @SerializedName("video")
            val video: String,
        )

        data class ProjectDetails(
            @SerializedName("age")
            val age: String,
            @SerializedName("bodyType")
            val bodyType: ArrayList<BodyType>,
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
            @SerializedName("heightFt")
            val heightFt: String,
            @SerializedName("heightIn")
            val heightIn: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("lang")
            val lang: ArrayList<Lang>,
            @SerializedName("skinTone")
            val skinTone: ArrayList<SkinTone>,
            @SerializedName("location")
            val location: String,
            @SerializedName("title")
            val title: String,
            @SerializedName("toDate")
            val toDate: String,
            @SerializedName("updated_at")
            val updatedAt: String
        ){
            data class BodyType(
                @SerializedName("id")
                val id: Int,
                @SerializedName("name")
                val name: String
            )

            data class Lang(
                @SerializedName("id")
                val id: Int,
                @SerializedName("name")
                val name: String
            )

            data class SkinTone(
                @SerializedName("id")
                val id: Int,
                @SerializedName("name")
                val name: String
            )
        }
    }
}