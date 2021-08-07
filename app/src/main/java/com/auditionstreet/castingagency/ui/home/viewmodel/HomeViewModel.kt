package com.auditionstreet.castingagency.ui.home.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.model.response.*
import com.auditionstreet.castingagency.ui.home.repository.HomeRepository
import com.auditionstreet.castingagency.ui.home.repository.ProjectRepository
import com.leo.wikireviews.utils.livedata.Event
import com.silo.model.request.AcceptRejectArtistRequest
import com.silo.model.request.BlockArtistRequest
import com.silo.model.request.LoginRequest
import com.silo.model.request.ProjectRequest
import com.silo.utils.network.NetworkHelper
import com.silo.utils.network.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class HomeViewModel @ViewModelInject constructor(
    private val homeRepository: HomeRepository,
    private val networkHelper: NetworkHelper,
) : ViewModel() {

    private val _get_home_screen_data = MutableLiveData<Event<Resource<HomeApiResponse>>>()
    val getHomeScreenData: LiveData<Event<Resource<HomeApiResponse>>>
        get() = _get_home_screen_data



    fun getHomeScreenData(url: String) {
        viewModelScope.launch {
            try {
                withTimeout(ApiConstant.API_TIMEOUT) {
                    _get_home_screen_data.postValue(
                        Event(
                            Resource.loading(
                                ApiConstant.GET_HOME_DATA,
                                null
                            )
                        )
                    )
                    if (networkHelper.isNetworkConnected()) {
                        homeRepository.getHomeScreenData(url).let {
                            if (it.isSuccessful && it.body() != null) {
                                _get_home_screen_data.postValue(
                                    Event(
                                        Resource.success(
                                            ApiConstant.GET_HOME_DATA,
                                            it.body()
                                        )
                                    )
                                )
                            } else {
                                _get_home_screen_data.postValue(
                                    Event(
                                        Resource.error(
                                            ApiConstant.GET_HOME_DATA,
                                            it.code(),
                                            it.errorBody().toString(),
                                            null
                                        )
                                    )
                                )
                            }
                        }
                    } else {
                        _get_home_screen_data.postValue(
                            Event(
                                Resource.requiredResource(
                                    ApiConstant.GET_HOME_DATA,
                                    R.string.err_no_network_available
                                )
                            )
                        )
                    }
                }
            }catch (e: Exception) {
                _get_home_screen_data.postValue(
                Event(
                    Resource.error(
                        ApiConstant.GET_HOME_DATA,
                        ApiConstant.STATUS_500,
                        "",
                        null
                    )
                )
            )
        }
        }
    }
}
