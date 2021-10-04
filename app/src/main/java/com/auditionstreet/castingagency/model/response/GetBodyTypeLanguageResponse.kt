package com.auditionstreet.castingagency.model.response
import com.google.gson.annotations.SerializedName


data class GetBodyTypeLanguageResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("msg")
    val msg: String
) {
    data class Data(
        @SerializedName("bodyTypes")
        val bodyTypes: ArrayList<BodyType>,
        @SerializedName("languages")
        val languages: ArrayList<Language>
    ) {
        data class BodyType(
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("name")
            val name: String,
            @SerializedName("updated_at")
            val updatedAt: String,
            @SerializedName("isChecked")
            var isChecked: Boolean = false
        )

        data class Language(
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("name")
            val name: String,
            @SerializedName("updated_at")
            val updatedAt: String,
            @SerializedName("isChecked")
            var isChecked: Boolean = false
        )
    }
}