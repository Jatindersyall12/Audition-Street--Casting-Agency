package com.auditionstreet.castingagency.ui.home.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.model.response.AddGroupResponse
import com.auditionstreet.castingagency.model.response.ApplicationResponse
import com.auditionstreet.castingagency.model.response.MyProjectResponse
import com.auditionstreet.castingagency.model.response.ProjectResponse
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

class ProjectViewModel @ViewModelInject constructor(
    private val projectRepository: ProjectRepository,
    private val networkHelper: NetworkHelper,
) : ViewModel() {
    val loginRequest = LoginRequest()

    private val _users = MutableLiveData<Event<Resource<ProjectResponse>>>()
    val users: LiveData<Event<Resource<ProjectResponse>>>
        get() = _users

    private val _accept_reject_artist = MutableLiveData<Event<Resource<AddGroupResponse>>>()
    val acceptRejectArtist: LiveData<Event<Resource<AddGroupResponse>>>
        get() = _accept_reject_artist

    private val _block_artist = MutableLiveData<Event<Resource<AddGroupResponse>>>()
    val blockArtist: LiveData<Event<Resource<AddGroupResponse>>>
        get() = _block_artist

    private val all_applications = MutableLiveData<Event<Resource<ApplicationResponse>>>()
    val allAppliactions: LiveData<Event<Resource<ApplicationResponse>>>
        get() = all_applications

    private val _my_projects = MutableLiveData<Event<Resource<MyProjectResponse>>>()
    val getMyProjects: LiveData<Event<Resource<MyProjectResponse>>>
        get() = _my_projects



     fun getProject(url: String) {
        viewModelScope.launch {
            _users.postValue(Event(Resource.loading(ApiConstant.GET_SHORTLISTED_LIST, null)))
            if (networkHelper.isNetworkConnected()) {
                projectRepository.getShortListedApp(url).let {
                    if (it.isSuccessful && it.body() != null) {
                        _users.postValue(
                            Event(
                                Resource.success(
                                    ApiConstant.GET_SHORTLISTED_LIST,
                                    it.body()
                                )
                            )
                        )
                    } else {
                        _users.postValue(
                            Event(
                                Resource.error(
                                    ApiConstant.GET_SHORTLISTED_LIST,
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

    fun getMyProject(url: String) {
        viewModelScope.launch {
            all_applications.postValue(Event(Resource.loading(ApiConstant.GET_MY_PROJECTS, null)))
            if (networkHelper.isNetworkConnected()) {
                projectRepository.getProjects(url).let {
                    if (it.isSuccessful && it.body() != null) {
                        _my_projects.postValue(
                            Event(
                                Resource.success(
                                    ApiConstant.GET_MY_PROJECTS,
                                    it.body()
                                )
                            )
                        )
                    } else {
                        _my_projects.postValue(
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

    fun getAllApplications(url: String) {
        viewModelScope.launch {
            all_applications.postValue(Event(Resource.loading(ApiConstant.GET_REQUEST_APPLICATIONS, null)))
            if (networkHelper.isNetworkConnected()) {
                projectRepository.getApplications(url).let {
                    if (it.isSuccessful && it.body() != null) {
                        all_applications.postValue(
                            Event(
                                Resource.success(
                                    ApiConstant.GET_REQUEST_APPLICATIONS,
                                    it.body()
                                )
                            )
                        )
                    } else {
                        all_applications.postValue(
                            Event(
                                Resource.error(
                                    ApiConstant.GET_REQUEST_APPLICATIONS,
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

     fun acceptRejectArtist(acceptRejectArtistRequest: AcceptRejectArtistRequest) {
        viewModelScope.launch {
            try {
                withTimeout(ApiConstant.API_TIMEOUT) {
                    _accept_reject_artist.postValue(
                        Event(
                            Resource.loading(
                                ApiConstant.ACCEPT_REJECT_ARTIST,
                                null
                            )
                        )
                    )
                    if (networkHelper.isNetworkConnected()) {
                        projectRepository.acceptRejectArtist(acceptRejectArtistRequest).let {
                            if (it.isSuccessful && it.body() != null) {
                                _accept_reject_artist.postValue(
                                    Event(
                                        Resource.success(
                                            ApiConstant.ACCEPT_REJECT_ARTIST,
                                            it.body()
                                        )
                                    )
                                )
                            } else {
                                _accept_reject_artist.postValue(
                                    Event(
                                        Resource.error(
                                            ApiConstant.ACCEPT_REJECT_ARTIST,
                                            it.code(),
                                            it.errorBody().toString(),
                                            null
                                        )
                                    )
                                )
                            }
                        }
                    } else {
                        _accept_reject_artist.postValue(
                            Event(
                                Resource.requiredResource(
                                    ApiConstant.ACCEPT_REJECT_ARTIST,
                                    R.string.err_no_network_available
                                )
                            )
                        )
                    }
                }
            }catch (e: Exception) {
                _accept_reject_artist.postValue(
                    Event(
                        Resource.error(
                            ApiConstant.ACCEPT_REJECT_ARTIST,
                            ApiConstant.STATUS_500,
                            "",
                            null
                        )
                    )
                )
            }
        }
    }

    fun blockArtist(blockArtistRequest: BlockArtistRequest) {
        viewModelScope.launch {
            try {
                withTimeout(ApiConstant.API_TIMEOUT) {
                    _block_artist.postValue(
                        Event(
                            Resource.loading(
                                ApiConstant.BLOCK_ARTIST,
                                null
                            )
                        )
                    )
                    if (networkHelper.isNetworkConnected()) {
                        projectRepository.blockArtist(blockArtistRequest).let {
                            if (it.isSuccessful && it.body() != null) {
                                _block_artist.postValue(
                                    Event(
                                        Resource.success(
                                            ApiConstant.BLOCK_ARTIST,
                                            it.body()
                                        )
                                    )
                                )
                            } else {
                                _block_artist.postValue(
                                    Event(
                                        Resource.error(
                                            ApiConstant.BLOCK_ARTIST,
                                            it.code(),
                                            it.errorBody().toString(),
                                            null
                                        )
                                    )
                                )
                            }
                        }
                    } else {
                        _block_artist.postValue(
                            Event(
                                Resource.requiredResource(
                                    ApiConstant.BLOCK_ARTIST,
                                    R.string.err_no_network_available
                                )
                            )
                        )
                    }
                }
            }catch (e: Exception) {
                _block_artist.postValue(
                    Event(
                        Resource.error(
                            ApiConstant.BLOCK_ARTIST,
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
