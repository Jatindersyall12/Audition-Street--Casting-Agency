package com.auditionstreet.castingagency.ui.projects.repository

import com.auditionstreet.castingagency.model.response.*
import com.silo.api.ApiService
import com.silo.model.request.AddGroupRequest
import com.silo.model.request.AddProjectRequest
import com.silo.model.request.UpdateProjectRequest
import retrofit2.Response
import javax.inject.Inject

class AddProjectRepository @Inject constructor(val apiService: ApiService) {
    suspend fun getAllAdmin(url: String):Response<AllAdminResponse> =apiService.getAllAdmin(url)
    suspend fun getAllUser(url: String):Response<AllUsersResponse> =apiService.getAllUser(url)
    suspend fun addProject(request: AddProjectRequest):Response<AddProjectResponse> =apiService.addProject(request)
    suspend fun addGroup(request: AddGroupRequest):Response<AddGroupResponse> =apiService.addGroup(request)
    suspend fun updateProject(request: UpdateProjectRequest):Response<AddProjectResponse> =apiService.updateProject(request)
    suspend fun getLanguageBodyType(url: String):Response<GetBodyTypeLanguageResponse> =apiService.getLanguageBodyType(url)


}
