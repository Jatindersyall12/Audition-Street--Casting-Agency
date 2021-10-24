package com.auditionstreet.castingagency.ui.login_signup.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.USER_DEFAULT_PASSWORD
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentSignupBinding
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.home.activity.FirstTimeHereActivity
import com.auditionstreet.castingagency.ui.home.activity.HomeActivity
import com.auditionstreet.castingagency.ui.login_signup.viewmodel.SignUpViewModel
import com.auditionstreet.castingagency.utils.AppConstants
import com.auditionstreet.castingagency.utils.CompressFile
import com.auditionstreet.castingagency.utils.chat.ChatHelper
import com.auditionstreet.castingagency.utils.chat.QbUsersHolder
import com.auditionstreet.castingagency.utils.chat.SharedPrefsHelper
import com.auditionstreet.castingagency.utils.showToast
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.leo.wikireviews.utils.livedata.EventObserver
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser
import com.silo.model.response.SignUpResponse
import com.silo.utils.AppBaseFragment
import com.silo.utils.network.Resource
import com.silo.utils.network.Status
import com.silo.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import java.io.File
import java.util.*
import javax.inject.Inject
private const val UNAUTHORIZED = 401
private const val DRAFT_LOGIN = "draft_login"
private const val DRAFT_USERNAME = "draft_username"
@AndroidEntryPoint
class SignUpFragment : AppBaseFragment(R.layout.fragment_signup), View.OnClickListener {
    private val binding by viewBinding(FragmentSignupBinding::bind)
    private val picker = 2000
    private var images: MutableList<com.esafirm.imagepicker.model.Image> = mutableListOf()
    private var profileImageFile: File? = null
    private var selectedImage = ""
    private val viewModel: SignUpViewModel by viewModels()
    private var compressImage = CompressFile()
    private var callbackManager: CallbackManager? = null
    private var email = ""


    @Inject
    lateinit var preferences: Preferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LoginManager.getInstance().logOut()
        initializeFacebookCallback()
        setListeners()
        setObservers()
    }

    private fun setListeners() {
        binding.imgProfileImage.setOnClickListener(this)
        binding.btnSignUp.setOnClickListener(this)
        binding.imgFacebook.setOnClickListener(this)
        binding.imgRound.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.imgProfileImage -> {
                pickImage()
            }
            binding.imgRound -> {
                pickImage()
            }

            binding.btnSignUp -> {
                email =  binding.etxEmail.text.toString()
                viewModel.isValidate(
                    binding.etxUserName.text.toString(),
                    binding.etxEmail.text.toString(),
                    binding.etxPassword.text.toString(),
                    binding.etxConfirmPassword.text.toString(),
                    requestSignUp(
                        resources.getString(R.string.str_facebook),
                        "",
                        resources.getString(R.string.str_false),
                        "",
                        ""
                    ), profileImageFile, selectedImage
                )
            }
            binding.imgFacebook -> {
                fbLogin()
            }
        }
    }

    private fun setObservers() {
        viewModel.signUp.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
    }

    private fun handleApiCallback(apiResponse: Resource<Any>) {
        when (apiResponse.status) {
            Status.SUCCESS -> {
                hideProgress()
                when (apiResponse.apiConstant) {
                    ApiConstant.SIGN_UP -> {
                        val signUpResponse = apiResponse.data as SignUpResponse
                        showToast(requireActivity(), signUpResponse.msg.toString())
                        preferences.setString(
                            AppConstants.USER_ID,
                            signUpResponse.data!![0]!!.id.toString()
                        )
                        preferences.setString(
                            AppConstants.USER_IMAGE,
                            signUpResponse.data[0]!!.image
                        )
                        // CHat Login Signup
                        prepareUser(signUpResponse.data[0]!!.name!!)
                        /*startActivity(Intent(requireActivity(), HomeActivity::class.java))
                        requireActivity().finish()*/
                    }
                }
            }
            Status.LOADING -> {
                showProgress()
            }
            Status.ERROR -> {
                hideProgress()
                showToast(requireContext(), apiResponse.message!!)
            }
            Status.RESOURCE -> {
                hideProgress()
                showToast(requireContext(), getString(apiResponse.resourceId!!))
            }

            else -> {

            }
        }
    }

    private fun pickImage() {
        ImagePicker.create(this)
            .returnMode(ReturnMode.ALL)
            .folderMode(false)
            .single()
            .limit(1)
            .toolbarFolderTitle(getString(R.string.folder))
            .toolbarImageTitle(getString(R.string.gallery_select_title_msg))
            .start(picker)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == picker && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            images = ImagePicker.getImages(data)
            binding.imgProfileImage.visibility = View.GONE
            profileImageFile = File(images[0].path)
            selectedImage = images[0].name
            profileImageFile =
                compressImage.getCompressedImageFile(profileImageFile!!, activity as Context)
            Glide.with(this).load(profileImageFile)
                .into(binding.imgRound)
        } else {
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun requestSignUp(
        socialType: String,
        socialId: String,
        isSocial: String,
        email: String,
        userName: String
    ): HashMap<String, RequestBody> {
        val map = HashMap<String, RequestBody>()
        if (socialId.isEmpty()) {
            map[resources.getString(R.string.str_username)] =
                toRequestBody(binding.etxUserName.text.toString().trim())
            map[resources.getString(R.string.str_useremail)] =
                toRequestBody(binding.etxEmail.text.toString().trim())
            map[resources.getString(R.string.str_password)] =
                toRequestBody(binding.etxPassword.text.toString().trim())
        } else {
            map[resources.getString(R.string.str_username)] =
                toRequestBody(userName)
            map[resources.getString(R.string.str_useremail)] =
                toRequestBody(email)
            map[resources.getString(R.string.str_password)] =
                toRequestBody("")
            selectedImage = ""
        }
        map[resources.getString(R.string.str_token)] =
            toRequestBody(preferences.getString(AppConstants.FIREBASE_ID))
        map[resources.getString(R.string.str_social_type)] =
            toRequestBody(socialType)
        map[resources.getString(R.string.str_socialId)] =
            toRequestBody(socialId)
        map[resources.getString(R.string.str_isSocial)] =
            toRequestBody(isSocial)

        return map
    }

    private fun toRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun initializeFacebookCallback() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    if (loginResult.accessToken != null) {
                        setFacebookData(loginResult)
                    }
                }

                override fun onCancel() {
                    showToast(
                        requireActivity(),
                        getString(R.string.err_facebook_authentication_fail)
                    )
                }

                override fun onError(error: FacebookException) {
                    showToast(
                        requireActivity(),
                        getString(R.string.err_facebook_authentication_fail)
                    )
                }

            })
    }

    private fun setFacebookData(loginResult: LoginResult) {
        val request = GraphRequest.newMeRequest(
            loginResult.accessToken
        ) { _, response ->
            try {
                email = response.jsonObject.getString(resources.getString(R.string.str_social_email))
                val first_name =
                    response.jsonObject.getString(resources.getString(R.string.str_social_first_name))
                this.viewModel.signUp(
                    requestSignUp(
                        resources.getString(R.string.str_facebook),
                        response.jsonObject.getString(resources.getString(R.string.str_social_id)),
                        resources.getString(R.string.str_true),
                        response.jsonObject.getString(resources.getString(R.string.str_social_email)),
                        first_name + " " + response.jsonObject.getString(resources.getString(R.string.str_social_last_name))
                    ), profileImageFile, selectedImage
                )
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,email,first_name,last_name")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun fbLogin() {
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken == null || accessToken.isExpired)
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("public_profile", "email"))
    }

    /*private fun saveDrafts() {
       SharedPrefsHelper.save(DRAFT_LOGIN, binding.etxEmail.text.toString())
       SharedPrefsHelper.save(DRAFT_USERNAME, usernameEt.text.toString())
   }*/

    private fun clearDrafts() {
        SharedPrefsHelper.save(DRAFT_LOGIN, "")
        SharedPrefsHelper.save(DRAFT_USERNAME, "")
    }

    private fun prepareUser(userName: String) {
        val qbUser = QBUser()
        qbUser.login = email
        qbUser.fullName = userName
        qbUser.password = USER_DEFAULT_PASSWORD
        signIn(qbUser)
    }

    private fun signIn(user: QBUser) {
        showProgress()
        ChatHelper.login(user, object : QBEntityCallback<QBUser> {
            override fun onSuccess(userFromRest: QBUser, bundle: Bundle?) {
                if (userFromRest.fullName != null && userFromRest.fullName == user.fullName) {
                    loginToChat(user)
                } else {
                    //Need to set password NULL, because server will update user only with NULL password
                    user.password = null
                    updateUser(user)
                }
            }

            override fun onError(e: QBResponseException) {
                if (e.httpStatusCode == UNAUTHORIZED) {
                    signUp(user)
                } else {
                    hideProgress()
                    showToast(requireActivity(), "Chat login error")
                }
            }
        })
    }

    private fun updateUser(user: QBUser) {
        ChatHelper.updateUser(user, object : QBEntityCallback<QBUser> {
            override fun onSuccess(qbUser: QBUser, bundle: Bundle?) {
                loginToChat(user)
            }

            override fun onError(e: QBResponseException) {
                hideProgress()
                showToast(requireActivity(), "Chat login error")
            }
        })
    }

    private fun loginToChat(user: QBUser) {
        //Need to set password, because the server will not register to chat without password
        user.password = USER_DEFAULT_PASSWORD
        ChatHelper.loginToChat(user, object : QBEntityCallback<Void> {
            override fun onSuccess(void: Void?, bundle: Bundle?) {
                SharedPrefsHelper.saveQbUser(user)
                //if (!chbSave.isChecked) {
                clearDrafts()
                //}
                QbUsersHolder.putUser(user)
                hideProgress()

                    startActivity(Intent(requireActivity(), FirstTimeHereActivity::class.java))
                    requireActivity().finish()
                    /*startActivity(Intent(requireActivity(), HomeActivity::class.java))
                    requireActivity().finish()*/
            }

            override fun onError(e: QBResponseException) {
                hideProgress()
                showToast(requireActivity(), "Chat login error")
            }
        })
    }

    private fun signUp(user: QBUser) {
        SharedPrefsHelper.removeQbUser()
        QBUsers.signUp(user).performAsync(object : QBEntityCallback<QBUser> {
            override fun onSuccess(p0: QBUser?, p1: Bundle?) {
                hideProgress()
                signIn(user)
            }

            override fun onError(exception: QBResponseException?) {
                hideProgress()
                showToast(requireActivity(), "Chat login error")
            }
        })
    }

}