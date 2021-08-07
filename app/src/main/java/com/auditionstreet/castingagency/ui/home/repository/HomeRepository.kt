package com.auditionstreet.castingagency.ui.home.repository

import com.auditionstreet.castingagency.model.response.*
import com.silo.api.ApiService
import com.silo.model.request.AcceptRejectArtistRequest
import com.silo.model.request.BlockArtistRequest
import com.silo.model.request.LoginRequest
import com.silo.model.request.ProjectRequest
import com.silo.model.response.LoginResponse
import retrofit2.Response
import javax.inject.Inject

class HomeRepository @Inject constructor(val apiService: ApiService) {
    suspend fun getHomeScreenData(url: String):Response<HomeApiResponse> =
        apiService.getHomeData(url)
}

