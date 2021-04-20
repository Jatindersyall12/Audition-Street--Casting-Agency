package com.silo.api

import com.auditionstreet.castingagency.api.ApiConstant.Companion.LOGIN
import com.auditionstreet.castingagency.api.ApiConstant.Companion.SIGN_UP
import com.silo.model.request.LoginRequest
import com.silo.model.request.SignupRequest
import com.silo.model.response.LoginResponse
import com.silo.model.response.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST



interface ApiService {
    @POST(LOGIN)
    suspend fun userLogin(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST(SIGN_UP)
    suspend fun signUpApi(@Body signUpRequest: SignupRequest): Response<SignUpResponse>

}