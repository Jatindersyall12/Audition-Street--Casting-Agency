package com.auditionstreet.castingagency.ui.profile.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.model.response.ProfileResponse
import com.auditionstreet.castingagency.ui.profile.repository.ProfileRepository
import com.leo.wikireviews.utils.livedata.Event
import com.silo.utils.network.NetworkHelper
import com.silo.utils.network.Resource
import kotlinx.coroutines.launch

class ProfileViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository,
    private val networkHelper: NetworkHelper,
) : ViewModel() {

    private val profile = MutableLiveData<Event<Resource<ProfileResponse>>>()
    val getProfile: LiveData<Event<Resource<ProfileResponse>>>
        get() = profile

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
}