package com.auditionstreet.castingagency.ui.projects.repository

import com.auditionstreet.castingagency.model.response.MyProjectResponse
import com.auditionstreet.castingagency.model.response.ProjectResponse
import com.silo.api.ApiService
import com.silo.model.request.LoginRequest
import com.silo.model.request.MyProjectRequest
import com.silo.model.request.ProjectRequest
import com.silo.model.response.LoginResponse
import retrofit2.Response
import javax.inject.Inject

class AddProjectRepository @Inject constructor(val apiService: ApiService) {
    suspend fun getMyProjects(projectRequest: MyProjectRequest):Response<MyProjectResponse> =apiService.getMyProjects(projectRequest)

}
