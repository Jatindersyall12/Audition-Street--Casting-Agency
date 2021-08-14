package com.auditionstreet.castingagency.ui.home.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.model.response.OtherProfileResponse
import com.auditionstreet.castingagency.ui.home.repository.OtherProfileRepository
import com.leo.wikireviews.utils.livedata.Event
import com.silo.model.request.WorkGalleryRequest
import com.silo.utils.network.NetworkHelper
import com.silo.utils.network.Resource
import kotlinx.coroutines.launch


class OtherProfileViewModel @ViewModelInject constructor(
    private val profileRepository: OtherProfileRepository,
    private val networkHelper: NetworkHelper,
) : ViewModel() {
    private val IMAGE_EXTENSION = "/*"

    private val profile = MutableLiveData<Event<Resource<OtherProfileResponse>>>()
    val getProfile: LiveData<Event<Resource<OtherProfileResponse>>>
        get() = profile


    fun getProfile(url: String) {
        viewModelScope.launch {
            profile.postValue(Event(Resource.loading(ApiConstant.GET_OTHER_PROFILE, null)))
            if (networkHelper.isNetworkConnected()) {
                profileRepository.getOtherProfile(url).let {
                    if (it.isSuccessful && it.body() != null) {
                        profile.postValue(
                            Event(
                                Resource.success(
                                    ApiConstant.GET_OTHER_PROFILE,
                                    it.body()
                                )
                            )
                        )
                    } else {
                        profile.postValue(
                            Event(
                                Resource.error(
                                    ApiConstant.GET_OTHER_PROFILE,
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