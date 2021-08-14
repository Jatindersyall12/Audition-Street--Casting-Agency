package com.auditionstreet.castingagency.ui.home.repository


import com.auditionstreet.castingagency.model.response.OtherProfileResponse
import com.silo.api.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.util.HashMap
import javax.inject.Inject

class OtherProfileRepository @Inject constructor(val apiService: ApiService) {
    suspend fun getOtherProfile(url: String):Response<OtherProfileResponse> =apiService.getOtherProfile(url)

}

