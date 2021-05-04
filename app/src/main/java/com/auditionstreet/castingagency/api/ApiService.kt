package com.silo.api

import com.auditionstreet.castingagency.api.ApiConstant.Companion.ADD_GROUP
import com.auditionstreet.castingagency.api.ApiConstant.Companion.ADD_PROJECT
import com.auditionstreet.castingagency.api.ApiConstant.Companion.GET_PROJECTS
import com.auditionstreet.castingagency.api.ApiConstant.Companion.LOGIN
import com.auditionstreet.castingagency.api.ApiConstant.Companion.SIGN_UP
import com.auditionstreet.castingagency.model.response.*
import com.silo.model.request.AddGroupRequest
import com.silo.model.request.AddProjectRequest
import com.silo.model.request.LoginRequest
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

    @POST(ADD_PROJECT)
    suspend fun addProject(@Body request: AddProjectRequest): Response<AddProjectResponse>

    @GET
    suspend fun getMyProjects(@Url url: String): Response<MyProjectResponse>

    @GET
    suspend fun getProfile(@Url url: String): Response<ProfileResponse>

    @GET
    suspend fun getAllAdmin(@Url url: String): Response<AllAdminResponse>


    @GET
    suspend fun getAllUser(@Url url: String): Response<AllUsersResponse>

    @GET
    suspend fun getMyProjectDetail(@Url url: String): Response<MyProjectDetailResponse>

    @POST(GET_PROJECTS)
    suspend fun getProjects(@Body projectRequest: ProjectRequest): Response<ProjectResponse>

    @POST(ADD_GROUP)
    suspend fun addGroup(@Body groupRequest: AddGroupRequest): Response<AddGroupResponse>


    @Multipart
    @POST(SIGN_UP)
    suspend fun signUp(
        @PartMap params: HashMap<String, RequestBody>,
        @Part photo: MultipartBody.Part?
    ): Response<SignUpResponse>

}