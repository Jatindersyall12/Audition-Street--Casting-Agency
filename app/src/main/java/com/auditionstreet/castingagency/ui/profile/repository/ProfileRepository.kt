package com.auditionstreet.castingagency.ui.profile.repository

import com.auditionstreet.castingagency.model.response.MyProjectResponse
import com.auditionstreet.castingagency.model.response.ProfileResponse
import com.auditionstreet.castingagency.model.response.ProjectResponse
import com.silo.api.ApiService
import com.silo.model.request.LoginRequest
import com.silo.model.request.MyProjectRequest
import com.silo.model.request.ProjectRequest
import com.silo.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Url
import javax.inject.Inject

class ProfileRepository @Inject constructor(val apiService: ApiService) {
    suspend fun getProfile(url: String):Response<ProfileResponse> =apiService.getProfile(url)

}

