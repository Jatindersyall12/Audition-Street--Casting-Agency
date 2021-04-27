package com.auditionstreet.castingagency.ui.projects.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.model.response.MyProjectDetailResponse
import com.auditionstreet.castingagency.ui.projects.repository.MyProjectDetailRepository
import com.leo.wikireviews.utils.livedata.Event
import com.silo.utils.network.NetworkHelper
import com.silo.utils.network.Resource
import kotlinx.coroutines.launch

class MyProjectDetailViewModel @ViewModelInject constructor(
    private val myProjectDetailRepository: MyProjectDetailRepository,
    private val networkHelper: NetworkHelper,
) : ViewModel() {

    private val _users = MutableLiveData<Event<Resource<MyProjectDetailResponse>>>()
    val users: LiveData<Event<Resource<MyProjectDetailResponse>>>
        get() = _users

     fun getMyProjectDetail(url: String) {
        viewModelScope.launch {
            _users.postValue(Event(Resource.loading(ApiConstant.GET_MY_PROJECTS, null)))
            if (networkHelper.isNetworkConnected()) {
                myProjectDetailRepository.getMyProjectDetail(url).let {
                    if (it.isSuccessful && it.body() != null) {
                        _users.postValue(
                            Event(
                                Resource.success(
                                    ApiConstant.GET_MY_PROJECTS,
                                    it.body()
                                )
                            )
                        )
                    } else {
                        _users.postValue(
                            Event(
                                Resource.error(
                                    ApiConstant.GET_MY_PROJECTS,
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
