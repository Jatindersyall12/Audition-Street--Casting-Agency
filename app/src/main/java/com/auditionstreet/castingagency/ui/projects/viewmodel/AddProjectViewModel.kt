package com.auditionstreet.castingagency.ui.projects.viewmodel

import android.text.TextUtils
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentAddProjectBinding
import com.auditionstreet.castingagency.model.response.AddProjectResponse
import com.auditionstreet.castingagency.model.response.AllAdminResponse
import com.auditionstreet.castingagency.model.response.AllUsersResponse
import com.auditionstreet.castingagency.ui.projects.repository.AddProjectRepository
import com.leo.wikireviews.utils.livedata.Event
import com.silo.model.request.AddProjectRequest
import com.silo.utils.network.NetworkHelper
import com.silo.utils.network.Resource
import kotlinx.coroutines.launch

class AddProjectViewModel @ViewModelInject constructor(
    private val addProjectRepository: AddProjectRepository,
    private val networkHelper: NetworkHelper,
) : ViewModel() {

    private val admin = MutableLiveData<Event<Resource<AllAdminResponse>>>()
    val allAdmin: LiveData<Event<Resource<AllAdminResponse>>>
        get() = admin

    private val user = MutableLiveData<Event<Resource<AllUsersResponse>>>()
    val allUser: LiveData<Event<Resource<AllUsersResponse>>>
        get() = user

    private val project = MutableLiveData<Event<Resource<AddProjectResponse>>>()
    val addProject: LiveData<Event<Resource<AddProjectResponse>>>
        get() = project

    fun getAllAdmin(url: String) {
        viewModelScope.launch {
            admin.postValue(Event(Resource.loading(ApiConstant.GET_ALL_ADMINS, null)))
            if (networkHelper.isNetworkConnected()) {
                addProjectRepository.getAllAdmin(url).let {
                    if (it.isSuccessful && it.body() != null) {
                        admin.postValue(
                            Event(
                                Resource.success(
                                    ApiConstant.GET_ALL_ADMINS,
                                    it.body()
                                )
                            )
                        )
                    } else {
                        admin.postValue(
                            Event(
                                Resource.error(
                                    ApiConstant.GET_ALL_ADMINS,
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

    fun getAllUser(url: String) {
        viewModelScope.launch {
            user.postValue(Event(Resource.loading(ApiConstant.GET_ALL_USER, null)))
            if (networkHelper.isNetworkConnected()) {
                addProjectRepository.getAllUser(url).let {
                    if (it.isSuccessful && it.body() != null) {
                        user.postValue(
                            Event(
                                Resource.success(
                                    ApiConstant.GET_ALL_USER,
                                    it.body()
                                )
                            )
                        )
                    } else {
                        user.postValue(
                            Event(
                                content = Resource.error(
                                    ApiConstant.GET_ALL_USER,
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

    fun addProject(request: AddProjectRequest) {
        viewModelScope.launch {
            this@AddProjectViewModel.project.postValue(
                Event(
                    Resource.loading(
                        ApiConstant.ADD_PROJECT,
                        null
                    )
                )
            )
            if (networkHelper.isNetworkConnected()) {
                addProjectRepository.addProject(request).let {
                    if (it.isSuccessful && it.body() != null) {
                        this@AddProjectViewModel.project.postValue(
                            Event(
                                Resource.success(
                                    ApiConstant.ADD_PROJECT,
                                    it.body()
                                )
                            )
                        )
                    } else {
                        this@AddProjectViewModel.project.postValue(
                            Event(
                                Resource.error(
                                    ApiConstant.ADD_PROJECT,
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

    fun isValidate(binding: FragmentAddProjectBinding): Boolean {
        when {
            TextUtils.isEmpty(binding.etxTitle.text!!.trim()) -> {
                project.postValue(
                    Event(
                        Resource.requiredResource(
                            ApiConstant.ADD_PROJECT, R
                                .string.str_error_title
                        )
                    )
                )
                return false
            }
            TextUtils.isEmpty(binding.etxDescription.text!!.trim()) -> {
                project.postValue(
                    Event(
                        Resource.requiredResource(
                            ApiConstant.ADD_PROJECT, R
                                .string.str_error_desc
                        )
                    )
                )
                return false
            }
            TextUtils.isEmpty(binding.etxBodyType.text!!.trim()) -> {
                project.postValue(
                    Event(
                        Resource.requiredResource(
                            ApiConstant.ADD_PROJECT, R
                                .string.str_error_body_type
                        )
                    )
                )
                return false
            }
            else -> return true
        }
    }
}
