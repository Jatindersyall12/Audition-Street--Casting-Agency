package com.silo.model.request
import com.google.gson.annotations.SerializedName

class AcceptRejectArtistRequest {
    @SerializedName("projectId")
    var projectId: String =""

    @SerializedName("userStatus")
    var userStatus: String=""

    @SerializedName("status")
    var status: String=""

    @SerializedName("id")
    var id: String=""

    @SerializedName("castingId")
    var castingId: String=""
}