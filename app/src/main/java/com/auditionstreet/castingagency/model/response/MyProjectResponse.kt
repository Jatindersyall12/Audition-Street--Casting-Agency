package com.auditionstreet.castingagency.model.response

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class MyProjectResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    var `data`: ArrayList<Data>,
    @SerializedName("message")
    val message: String
) {
    data class Data(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("lap_time")
        val lapTime: String,
        @SerializedName("lat")
        val lat: String,
        @SerializedName("lon")
        val lon: String,
        @SerializedName("track_name")
        val name: String,
        @SerializedName("track_date")
        val trackDate: String,
        @SerializedName("track_time")
        val trackTime: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_id")
        val userId: Int,
        @SerializedName("user_track_id")
        val userTrackId: Int,
        @SerializedName("vehicle_id")
        val vehicleId: String,
        @SerializedName("video")
        val video: String,
        @SerializedName("event_name")
        var eventName: String,
        @SerializedName("is_favourite")
        var isFavourite: String,
        @SerializedName("track_image")
        var trackImage: String
    )
}