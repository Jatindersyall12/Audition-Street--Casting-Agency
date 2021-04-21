package com.auditionstreet.castingagency.ui.login_signup.repository

import com.silo.api.ApiService
import com.silo.model.request.LoginRequest
import com.silo.model.response.LoginResponse
import retrofit2.Response
import javax.inject.Inject

class LoginRepository @Inject constructor(val apiService: ApiService) {
    suspend fun loginUser(loginRequest: LoginRequest):Response<LoginResponse> =apiService.userLogin(loginRequest)

}

