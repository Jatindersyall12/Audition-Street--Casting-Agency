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
import com.auditionstreet.castingagency.ui.login_signup.repository.SignUpRepository
import com.leo.wikireviews.utils.livedata.Event
import com.silo.model.request.LoginRequest
import com.silo.model.response.LoginResponse
import com.silo.model.response.SignUpResponse
import com.silo.utils.network.NetworkHelper
import com.silo.utils.network.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class SignUpViewModel @ViewModelInject constructor(
    private val signUpRepository: SignUpRepository,
    private val networkHelper: NetworkHelper,
): ViewModel() {
    val loginRequest = LoginRequest()
    private val IMAGE_EXTENSION = "/*"

    private val _sign_up = MutableLiveData<Event<Resource<SignUpResponse>>>()
    val signUp: LiveData<Event<Resource<SignUpResponse>>>
        get() = _sign_up


    fun signUp(
        map: java.util.HashMap<String, RequestBody>, carImage: File?, selectedImage: String
    ) {
        viewModelScope.launch {
            _sign_up.postValue(Event(Resource.loading(ApiConstant.SIGN_UP, null)))
            var photo: MultipartBody.Part? = null
            if (networkHelper.isNetworkConnected()) {
                if (!selectedImage.isEmpty()) {
                    val profileImage =
                        RequestBody.create(
                            IMAGE_EXTENSION.toMediaTypeOrNull(),
                            carImage!!
                        )
                    photo =
                        MultipartBody.Part.createFormData(
                            "photo",
                            selectedImage,
                            profileImage
                        )
                }
                signUpRepository.updateProfile(map, photo).let {
                    if (it.isSuccessful && it.body() != null) {
                        _sign_up.postValue(
                            (Event(
                                Resource.success(
                                    ApiConstant.SIGN_UP,
                                    it.body()
                                )
                            ))
                        )
                    } else {
                        _sign_up.postValue(
                            Event(
                                Resource.error(
                                    ApiConstant.SIGN_UP,
                                    it.code(),
                                    it.errorBody().toString(),
                                    null
                                )
                            )
                        )
                    }
                }

            } else {
                _sign_up.postValue(
                    Event(
                        Resource.requiredResource(
                            ApiConstant.SIGN_UP,
                            R.string.err_no_network_available
                        )
                    )
                )
            }
        }

        /* fun isValidate(email: String, password: String) {
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
    }*/
    }
}
