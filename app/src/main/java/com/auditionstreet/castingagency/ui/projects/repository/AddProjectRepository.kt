package com.auditionstreet.castingagency.ui.projects.repository

import com.auditionstreet.castingagency.model.response.AddProjectResponse
import com.auditionstreet.castingagency.model.response.AllAdminResponse
import com.auditionstreet.castingagency.model.response.AllUsersResponse
import com.silo.api.ApiService
import com.silo.model.request.AddProjectRequest
import retrofit2.Response
import javax.inject.Inject

class AddProjectRepository @Inject constructor(val apiService: ApiService) {
    suspend fun getAllAdmin(url: String):Response<AllAdminResponse> =apiService.getAllAdmin(url)
    suspend fun getAllUser(url: String):Response<AllUsersResponse> =apiService.getAllUser(url)
    suspend fun addProject(request: AddProjectRequest):Response<AddProjectResponse> =apiService.addProject(request)

}
