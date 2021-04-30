package com.auditionstreet.castingagency.ui.projects.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.model.response.AllUsersResponse
import com.auditionstreet.castingagency.model.response.MyProjectResponse
import com.auditionstreet.castingagency.ui.projects.repository.AddProjectRepository
import com.auditionstreet.castingagency.ui.projects.repository.MyProjectRepository
import com.leo.wikireviews.utils.livedata.Event
import com.silo.model.request.LoginRequest
import com.silo.model.request.MyProjectRequest
import com.silo.model.request.ProjectRequest
import com.silo.utils.network.NetworkHelper
import com.silo.utils.network.Resource
import kotlinx.coroutines.launch

class AddProjectViewModel @ViewModelInject constructor(
    private val addProjectRepository: AddProjectRepository,
    private val networkHelper: NetworkHelper,
) : ViewModel() {

    private val _all_user = MutableLiveData<Event<Resource<AllUsersResponse>>>()
    val allUsers: LiveData<Event<Resource<AllUsersResponse>>>
        get() = _all_user

    fun getAllUsers(url: String) {
        viewModelScope.launch {
            _all_user.postValue(Event(Resource.loading(ApiConstant.GET_ALL_USERS, null)))
            if (networkHelper.isNetworkConnected()) {
                addProjectRepository.getAllUsers(url).let {
                    if (it.isSuccessful && it.body() != null) {
                        _all_user.postValue(
                            Event(
                                Resource.success(
                                    ApiConstant.GET_ALL_USERS,
                                    it.body()
                                )
                            )
                        )
                    } else {
                        _all_user.postValue(
                            Event(
                                Resource.error(
                                    ApiConstant.GET_ALL_USERS,
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
