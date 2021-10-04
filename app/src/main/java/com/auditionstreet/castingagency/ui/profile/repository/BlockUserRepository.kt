package com.auditionstreet.castingagency.ui.profile.repository

import com.auditionstreet.castingagency.model.response.BlockUserListResponse
import com.auditionstreet.castingagency.model.response.DeleteMediaResponse
import com.auditionstreet.castingagency.model.response.ProfileResponse
import com.auditionstreet.castingagency.model.response.UploadMediaResponse
import com.silo.api.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.util.HashMap
import javax.inject.Inject

class BlockUserRepository @Inject constructor(val apiService: ApiService) {
    suspend fun getBlockUserList(url: String): Response<BlockUserListResponse> = apiService.getBlockUserList(url)


}

