package com.auditionstreet.castingagency.ui.home.repository

import com.auditionstreet.castingagency.model.response.AddGroupResponse
import com.auditionstreet.castingagency.model.response.ApplicationResponse
import com.auditionstreet.castingagency.model.response.MyProjectResponse
import com.auditionstreet.castingagency.model.response.ProjectResponse
import com.silo.api.ApiService
import com.silo.model.request.AcceptRejectArtistRequest
import com.silo.model.request.BlockArtistRequest
import com.silo.model.request.LoginRequest
import com.silo.model.request.ProjectRequest
import com.silo.model.response.LoginResponse
import retrofit2.Response
import javax.inject.Inject

class ProjectRepository @Inject constructor(val apiService: ApiService) {
    suspend fun getShortListedApp(url: String):Response<ProjectResponse> =apiService.getProjects(url)
    suspend fun getApplications(url: String):Response<ApplicationResponse> =apiService.getApplications(url)
    suspend fun getProjects(url: String):Response<MyProjectResponse> =apiService.getMyProjects(url)
    suspend fun acceptRejectArtist(acceptRejectArtistRequest: AcceptRejectArtistRequest):Response<AddGroupResponse> =apiService.acceptRejectArtist(acceptRejectArtistRequest)
    suspend fun blockArtist(blockArtistRequest: BlockArtistRequest):Response<AddGroupResponse> =apiService.blockArtist(blockArtistRequest)


}

