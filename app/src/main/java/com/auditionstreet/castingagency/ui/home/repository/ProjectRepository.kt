package com.auditionstreet.castingagency.ui.home.repository

import com.auditionstreet.castingagency.model.response.MyProjectResponse
import com.auditionstreet.castingagency.model.response.ProjectResponse
import com.silo.api.ApiService
import com.silo.model.request.LoginRequest
import com.silo.model.request.ProjectRequest
import com.silo.model.response.LoginResponse
import retrofit2.Response
import javax.inject.Inject

class ProjectRepository @Inject constructor(val apiService: ApiService) {
    suspend fun getProjects(projectRequest: ProjectRequest):Response<ProjectResponse> =apiService.getProjects(projectRequest)
    suspend fun getMyProjects(url: String):Response<MyProjectResponse> =apiService.getMyProjects(url)

}

