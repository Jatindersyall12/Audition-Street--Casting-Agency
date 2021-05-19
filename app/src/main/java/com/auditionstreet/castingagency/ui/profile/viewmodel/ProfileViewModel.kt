package com.auditionstreet.castingagency.ui.profile.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.model.response.ProfileResponse
import com.auditionstreet.castingagency.model.response.UploadMediaResponse
import com.auditionstreet.castingagency.ui.profile.repository.ProfileRepository
import com.leo.wikireviews.utils.livedata.Event
import com.silo.model.request.WorkGalleryRequest
import com.silo.utils.network.NetworkHelper
import com.silo.utils.network.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ProfileViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository,
    private val networkHelper: NetworkHelper,
) : ViewModel() {
    private val IMAGE_EXTENSION = "/*"

    private val profile = MutableLiveData<Event<Resource<ProfileResponse>>>()
    val getProfile: LiveData<Event<Resource<ProfileResponse>>>
        get() = profile

    private val upload_media = MutableLiveData<Event<Resource<UploadMediaResponse>>>()
    val uploadMedia: LiveData<Event<Resource<UploadMediaResponse>>>
        get() = upload_media


    fun getProfile(url: String) {
        viewModelScope.launch {
            profile.postValue(Event(Resource.loading(ApiConstant.GET_PROFILE, null)))
            if (networkHelper.isNetworkConnected()) {
                profileRepository.getProfile(url).let {
                    if (it.isSuccessful && it.body() != null) {
                        profile.postValue(
                            Event(
                                Resource.success(
                                    ApiConstant.GET_PROFILE,
                                    it.body()
                                )
                            )
                        )
                    } else {
                        profile.postValue(
                            Event(
                                Resource.error(
                                    ApiConstant.GET_PROFILE,
                                    it.code(),
                                    it.errorBody().toString(),
                                    null
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    fun uploadMedia(list: ArrayList<WorkGalleryRequest>) {
        viewModelScope.launch {
            upload_media.postValue(Event(Resource.loading(ApiConstant.UPLOAD_MEDIA, null)))
            var photo: MultipartBody.Part? = null
            val mediaList = ArrayList<MultipartBody.Part>()
            if (networkHelper.isNetworkConnected()) {
                    for(i in 0..list.size-1)
                    {
                        val videoOrImage = File(list.get(i).path)
                        val profileImage =
                            RequestBody.create(
                                IMAGE_EXTENSION.toMediaTypeOrNull(),
                                videoOrImage!!
                            )
                        photo =
                            MultipartBody.Part.createFormData(
                                "media",
                                "sd",
                                profileImage
                            )
                        mediaList.add(photo)
                    }
                profileRepository.uploadMedia(mediaList).let {
                    if (it.isSuccessful && it.body() != null) {
                        upload_media.postValue(
                            (Event(
                                Resource.success(
                                    ApiConstant.UPLOAD_MEDIA,
                                    it.body()
                                )
                            ))
                        )
                    } else {
                        upload_media.postValue(
                            Event(
                                Resource.error(
                                    ApiConstant.UPLOAD_MEDIA,
                                    it.code(),
                                    it.errorBody().toString(),
                                    null
                                )
                            )
                        )
                    }
                }

            }
        }
    }
}