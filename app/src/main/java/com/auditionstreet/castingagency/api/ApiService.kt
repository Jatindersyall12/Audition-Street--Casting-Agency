package com.silo.api

import com.auditionstreet.castingagency.api.ApiConstant.Companion.LOGIN
import com.auditionstreet.castingagency.api.ApiConstant.Companion.SIGN_UP
import com.silo.model.request.LoginRequest
import com.silo.model.response.LoginResponse
import com.silo.model.response.SignUpResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.util.*


interface ApiService {
    @POST(LOGIN)
    suspend fun userLogin(@Body loginRequest: LoginRequest): Response<LoginResponse>


    @Multipart
    @POST(SIGN_UP)
    suspend fun signUp(
        @PartMap params: HashMap<String, RequestBody>,
        @Part photo: MultipartBody.Part?
    ): Response<SignUpResponse>

}