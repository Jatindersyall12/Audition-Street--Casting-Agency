package com.auditionstreet.castingagency.ui.login_signup.viewmodel

import android.text.TextUtils
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.ui.login_signup.repository.LoginRepository
import com.silo.model.request.LoginRequest
import com.silo.model.response.LoginResponse
import com.silo.utils.network.NetworkHelper
import com.silo.utils.network.Resource
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val loginRepository: LoginRepository,
    private val networkHelper: NetworkHelper,
): ViewModel() {
    val loginRequest = LoginRequest()

    private val _users = MutableLiveData<Resource<LoginResponse>>()
    val users: LiveData<Resource<LoginResponse>>
        get() = _users

    private fun authorizedUser(loginRequest: LoginRequest) {
        viewModelScope.launch {
            _users.postValue(Resource.loading(ApiConstant.LOGIN,null))
            if (networkHelper.isNetworkConnected()) {
                loginRepository.loginUser(loginRequest).let {
                    if (it.isSuccessful && it.body()!=null) {
                        _users.postValue(Resource.success(ApiConstant.LOGIN,it.body()))
                    }else {
                        if (it.code() == ApiConstant.STATUS_302) {
                        _users.postValue(Resource.requiredResource(ApiConstant.LOGIN, R.string.err_invalid_credentials))
                        }
                        else
                            _users.postValue(Resource.error(ApiConstant.LOGIN,it.code(),it.errorBody().toString(),null))
                    }
                }
            } else{
                _users.postValue(Resource.requiredResource(ApiConstant.LOGIN, R.string.err_no_network_available))
            }
        }
    }

    fun isValidate(email: String, password: String) {
        loginRequest.email = email
        loginRequest.password = password
        if (TextUtils.isEmpty(loginRequest.email)) {
            _users.postValue(
                Resource.requiredResource(
                    ApiConstant.LOGIN, R
                        .string.err_empty_email
                )
            )
            return
        } else if (TextUtils.isEmpty(loginRequest.password)) {
            _users.postValue(
                Resource.requiredResource(
                    ApiConstant.LOGIN,
                    R.string.err_empty_password
                )
            )
            return
        }
        authorizedUser(loginRequest)
    }
}
