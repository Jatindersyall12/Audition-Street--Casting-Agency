@file:JvmName("ApiConstants")
@file:JvmMultifileClass

package com.auditionstreet.castingagency.api
class ApiConstant {

    companion object {
        /*Api Header*/
        const val CONTENT_TYPE = "Content-Type"
        const val ACCEPT = "accept"
        const val APPLICATION_JSON = "application/json"
        const val PLATFORM = "platform"
        const val PLATFORM_TYPE = "1"

        const val STATUS_200 = 200
        const val STATUS_201 = 201

        const val STATUS_400 = 400
        const val STATUS_404 = 400
        const val STATUS_500 = 500

        const val SUCCESS = "Success"
        const val ERROR = "Error"

        const val VALIDATION_FAIL = "Validation_Fail"
        const val API_TIMEOUT = 30000L

        const val IMAGE_ENTITY = "image/jpeg"
        const val VIDEO_ENTITY = "video/mp4"
        const val FORM_DATA = "multipart/form-data"
        const val PUBLIC_PROFILE = "&public_profile=true"

        const val LOGIN = "api/v1/user/login"
        const val GET_PROJECTS = "api/v1/user/sign-up"
        const val SIGN_UP = "api/v1/user/sign-up"

        const val IS_USER_EXIST = "api/v1/user/check-username-exist"
    }}


