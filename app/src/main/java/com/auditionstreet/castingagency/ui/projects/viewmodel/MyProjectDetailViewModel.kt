package com.auditionstreet.castingagency.ui.projects.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.model.response.CommonResponse
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

    private val _delete_project = MutableLiveData<Event<Resource<CommonResponse>>>()
    val deleteProject: LiveData<Event<Resource<CommonResponse>>>
        get() = _delete_project

     fun getMyProjectDetail(url: String) {
        viewModelScope.launch {
            _users.postValue(Event(Resource.loading(ApiConstant.GET_MY_PROJECTS, null)))
            if (networkHelper.isNetworkConnected()) {
                myProjectDetailRepository.getMyProjectDetail(url).let {
                    if (it.isSuccessful && it.body() != null) {
                        _users.postValue(
                            Event(
                                Resource.success(
                                    ApiConstant.GET_MY_PROJECTS_DETAILS,
                                    it.body()
                                )
                            )
                        )
                    } else {
                        _users.postValue(
                            Event(
                                Resource.error(
                                    ApiConstant.GET_MY_PROJECTS_DETAILS,
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

    fun deleteProject(url: String) {
        viewModelScope.launch {
            _delete_project.postValue(Event(Resource.loading(ApiConstant.DELETE_PROJECT, null)))
            if (networkHelper.isNetworkConnected()) {
                myProjectDetailRepository.deleteProject(url).let {
                    if (it.isSuccessful && it.body() != null) {
                        _delete_project.postValue(
                            Event(
                                Resource.success(
                                    ApiConstant.DELETE_PROJECT,
                                    it.body()
                                )
                            )
                        )
                    } else {
                        _delete_project.postValue(
                            Event(
                                Resource.error(
                                    ApiConstant.DELETE_PROJECT,
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
