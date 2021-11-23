package com.auditionstreet.castingagency.ui.profile.repository

import com.auditionstreet.castingagency.model.response.DeleteMediaResponse
import com.auditionstreet.castingagency.model.response.ProfileResponse
import com.auditionstreet.castingagency.model.response.UploadMediaResponse
import com.silo.api.ApiService
import com.silo.model.request.LogoutRequest
import com.silo.model.request.SupportRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.util.HashMap
import javax.inject.Inject

class ProfileRepository @Inject constructor(val apiService: ApiService) {
    suspend fun getProfile(url: String): Response<ProfileResponse> = apiService.getProfile(url)
    suspend fun uploadMedia(
        media: List<MultipartBody.Part?>,
        profileImageFile: MultipartBody.Part?,
        requestProfileUpdate: HashMap<String, RequestBody>
    ): Response<UploadMediaResponse> =
        apiService.uploadMedia(media,requestProfileUpdate,profileImageFile)
    suspend fun deleteMedia(url: String):Response<DeleteMediaResponse> =apiService.deleteMedia(url)

    suspend fun addSupport(supportRequest: SupportRequest):
            Response<DeleteMediaResponse> =apiService.addSupport(supportRequest)

    suspend fun logout(logoutRequest: LogoutRequest):
            Response<DeleteMediaResponse> =apiService.logout(logoutRequest)

}

