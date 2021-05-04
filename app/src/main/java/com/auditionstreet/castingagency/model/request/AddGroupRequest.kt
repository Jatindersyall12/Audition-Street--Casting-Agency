package com.silo.model.request
import com.google.gson.annotations.SerializedName


 class AddGroupRequest
 {
    @SerializedName("anotherCastingId")
    lateinit var anotherCastingId: List<String>
    @SerializedName("castingId")
    var castingId: String = ""
 }