package com.auditionstreet.castingagency.ui.home.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.model.response.ProjectResponse
import com.auditionstreet.castingagency.ui.home.repository.ProjectRepository
import com.leo.wikireviews.utils.livedata.Event
import com.silo.model.request.LoginRequest
import com.silo.model.request.ProjectRequest
import com.silo.utils.network.NetworkHelper
import com.silo.utils.network.Resource
import kotlinx.coroutines.launch

class ProjectViewModel @ViewModelInject constructor(
    private val projectRepository: ProjectRepository,
    private val networkHelper: NetworkHelper,
) : ViewModel() {
    val loginRequest = LoginRequest()

    private val _users = MutableLiveData<Event<Resource<ProjectResponse>>>()
    val users: LiveData<Event<Resource<ProjectResponse>>>
        get() = _users

    private fun getProject(projectRequest: ProjectRequest) {
        viewModelScope.launch {
            _users.postValue(Event(Resource.loading(ApiConstant.GET_PROJECTS, null)))
            if (networkHelper.isNetworkConnected()) {
                projectRepository.getProjects(projectRequest).let {
                    if (it.isSuccessful && it.body() != null) {
                        _users.postValue(
                            Event(
                                Resource.success(
                                    ApiConstant.GET_PROJECTS,
                                    it.body()
                                )
                            )
                        )
                    } else {
                        _users.postValue(
                            Event(
                                Resource.error(
                                    ApiConstant.GET_PROJECTS,
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
