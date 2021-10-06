package com.silo.model.request
import com.google.gson.annotations.SerializedName

class BlockArtistRequest {
    @SerializedName("castingId")
    var castingId: String =""

    @SerializedName("artistId")
    var artistId: String=""

    @SerializedName("status")
    var status: String=""

}