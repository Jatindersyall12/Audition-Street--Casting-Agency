package com.auditionstreet.castingagency.ui.login_signup.repository

import com.silo.api.ApiService
import com.silo.model.request.LoginRequest
import com.silo.model.response.LoginResponse
import com.silo.model.response.SignUpResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class SignUpRepository @Inject constructor(val apiService: ApiService) {
    suspend fun updateProfile(params: HashMap<String, RequestBody>, photo: MultipartBody.Part?): Response<SignUpResponse> = apiService.signUp(params, photo)
}

