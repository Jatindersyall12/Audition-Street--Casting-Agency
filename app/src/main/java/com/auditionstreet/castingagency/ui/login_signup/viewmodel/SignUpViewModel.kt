package com.auditionstreet.castingagency.ui.login_signup.viewmodel

import android.text.TextUtils
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.ui.login_signup.repository.SignUpRepository
import com.leo.wikireviews.utils.livedata.Event
import com.silo.model.response.SignUpResponse
import com.silo.utils.isValidEmail
import com.silo.utils.network.NetworkHelper
import com.silo.utils.network.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

class SignUpViewModel @ViewModelInject constructor(
    private val signUpRepository: SignUpRepository,
    private val networkHelper: NetworkHelper,
) : ViewModel() {
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
                            "image",
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
                        if (it.code() == ApiConstant.STATUS_302) {
                            _sign_up.postValue(
                                Event(
                                    Resource.requiredResource(
                                        ApiConstant.SIGN_UP,
                                        R.string.err_email_msg
                                    )
                                )
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
    }

    fun isValidate(
        userName: String,
        email: String,
        password: String,
        confirmPassword: String,
        requestSignUp: HashMap<String, RequestBody>,
        profileImageFile: File?,
        selectedImage: String
    ) {
        if (TextUtils.isEmpty(userName)) {
            _sign_up.postValue(
                Event(
                    Resource.requiredResource(
                        ApiConstant.SIGN_UP,
                        R.string.err_user_name
                    )
                )
            )
            return
        } else if (TextUtils.isEmpty(email)) {
            _sign_up.postValue(
                Event(
                    Resource.requiredResource(
                        ApiConstant.SIGN_UP,
                        R.string.err_email
                    )
                )
            )
            return
        } else if (TextUtils.isEmpty(password)) {
            _sign_up.postValue(
                Event(
                    Resource.requiredResource(
                        ApiConstant.SIGN_UP,
                        R.string.err_password
                    )
                )
            )
            return
        } else if (TextUtils.isEmpty(confirmPassword)) {
            _sign_up.postValue(
                Event(
                    Resource.requiredResource(
                        ApiConstant.SIGN_UP,
                        R.string.err_confirm_password
                    )
                )
            )
            return
        } else if (!isValidEmail(email)) {
            _sign_up.postValue(
                Event(
                    Resource.requiredResource(
                        ApiConstant.SIGN_UP,
                        R.string.err_valid_email
                    )
                )
            )
            return
        } else if (password.length < 6) {
            _sign_up.postValue(
                Event(
                    Resource.requiredResource(
                        ApiConstant.SIGN_UP,
                        R.string.err_password_lenght
                    )
                )
            )
            return
        } else if (!password.equals(confirmPassword)) {
            _sign_up.postValue(
                Event(
                    Resource.requiredResource(
                        ApiConstant.SIGN_UP,
                        R.string.err_passwrd_confirm
                    )
                )
            )
            return
        } else
            signUp(requestSignUp, profileImageFile, selectedImage)
    }
}

