package com.auditionstreet.castingagency.ui.profile.repository

import com.auditionstreet.castingagency.model.response.*
import com.silo.api.ApiService
import com.silo.model.request.BlockArtistRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.util.HashMap
import javax.inject.Inject

class BlockUserRepository @Inject constructor(val apiService: ApiService) {
    suspend fun getBlockUserList(url: String): Response<BlockUserListResponse> = apiService.getBlockUserList(url)
    suspend fun blockArtist(blockArtistRequest: BlockArtistRequest):Response<AddGroupResponse> =apiService.blockArtist(blockArtistRequest)


}

