package com.auditionstreet.castingagency.ui.projects.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.model.response.MyProjectResponse
import com.auditionstreet.castingagency.ui.projects.repository.MyProjectRepository
import com.leo.wikireviews.utils.livedata.Event
import com.silo.model.request.LoginRequest
import com.silo.model.request.MyProjectRequest
import com.silo.model.request.ProjectRequest
import com.silo.utils.network.NetworkHelper
import com.silo.utils.network.Resource
import kotlinx.coroutines.launch

class AddProjectViewModel @ViewModelInject constructor(
    private val myProjectRepository: MyProjectRepository,
    private val networkHelper: NetworkHelper,
) : ViewModel() {

    private val _users = MutableLiveData<Event<Resource<MyProjectResponse>>>()
    val users: LiveData<Event<Resource<MyProjectResponse>>>
        get() = _users

     fun getMyProject(myProjectRequest: MyProjectRequest) {
        viewModelScope.launch {
            _users.postValue(Event(Resource.loading(ApiConstant.GET_PROJECTS, null)))
            if (networkHelper.isNetworkConnected()) {
                myProjectRepository.getMyProjects(myProjectRequest).let {
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
