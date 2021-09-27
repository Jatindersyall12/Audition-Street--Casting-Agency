package com.auditionstreet.castingagency.ui.projects.repository

import com.auditionstreet.castingagency.model.response.CommonResponse
import com.auditionstreet.castingagency.model.response.MyProjectDetailResponse
import com.auditionstreet.castingagency.model.response.MyProjectResponse
import com.auditionstreet.castingagency.model.response.ProjectResponse
import com.silo.api.ApiService
import com.silo.model.request.LoginRequest
import com.silo.model.request.MyProjectRequest
import com.silo.model.request.ProjectRequest
import com.silo.model.response.LoginResponse
import retrofit2.Response
import javax.inject.Inject

class MyProjectDetailRepository @Inject constructor(val apiService: ApiService) {
   suspend fun getMyProjectDetail(url: String):Response<MyProjectDetailResponse> =apiService.getMyProjectDetail(url)
   suspend fun deleteProject(url: String):Response<CommonResponse> =apiService.deleteProject(url)

}
