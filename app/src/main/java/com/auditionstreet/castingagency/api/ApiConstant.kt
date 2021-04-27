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
        const val STATUS_302 = 302

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

        const val LOGIN = "castingLogin"
        const val GET_PROJECTS = ""
        const val GET_MY_PROJECTS = "projectList"
        const val GET_MY_PROJECTS_DETAILS = "projectDetail"

        const val ADD_PROJECT = ""

        const val SIGN_UP = "castingSignup"

        const val IS_USER_EXIST = "api/v1/user/check-username-exist"
    }}


