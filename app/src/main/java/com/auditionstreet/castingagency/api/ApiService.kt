package com.silo.api

import com.auditionstreet.castingagency.api.ApiConstant.Companion.GET_MY_PROJECTS
import com.auditionstreet.castingagency.api.ApiConstant.Companion.GET_PROJECTS
import com.auditionstreet.castingagency.api.ApiConstant.Companion.LOGIN
import com.auditionstreet.castingagency.api.ApiConstant.Companion.SIGN_UP
import com.auditionstreet.castingagency.model.response.MyProjectDetailResponse
import com.auditionstreet.castingagency.model.response.MyProjectResponse
import com.auditionstreet.castingagency.model.response.ProjectResponse
import com.silo.model.request.LoginRequest
import com.silo.model.request.MyProjectRequest
import com.silo.model.request.ProjectRequest
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

    @GET
    suspend fun getMyProjects(@Url url: String): Response<MyProjectResponse>

    @GET
    suspend fun getMyProjectDetail(@Url url: String): Response<MyProjectDetailResponse>

    @POST(GET_PROJECTS)
    suspend fun getProjects(@Body projectRequest: ProjectRequest): Response<ProjectResponse>


    @Multipart
    @POST(SIGN_UP)
    suspend fun signUp(
        @PartMap params: HashMap<String, RequestBody>,
        @Part photo: MultipartBody.Part?
    ): Response<SignUpResponse>

}