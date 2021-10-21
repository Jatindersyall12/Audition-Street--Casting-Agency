package com.auditionstreet.castingagency.ui.login_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.USER_DEFAULT_PASSWORD
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentSigninBinding
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.home.activity.HomeActivity
import com.auditionstreet.castingagency.ui.login_signup.viewmodel.LoginViewModel
import com.auditionstreet.castingagency.utils.AppConstants
import com.auditionstreet.castingagency.utils.chat.ChatHelper
import com.auditionstreet.castingagency.utils.chat.QbUsersHolder
import com.auditionstreet.castingagency.utils.chat.SharedPrefsHelper
import com.auditionstreet.castingagency.utils.showProgressDialog
import com.auditionstreet.castingagency.utils.showToast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser
import com.silo.model.request.LoginRequest
import com.silo.model.response.LoginResponse
import com.silo.utils.AppBaseFragment
import com.silo.utils.network.Resource
import com.silo.utils.network.Status
import com.silo.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import javax.inject.Inject

private const val UNAUTHORIZED = 401
private const val DRAFT_LOGIN = "draft_login"
private const val DRAFT_USERNAME = "draft_username"

@AndroidEntryPoint
class SignInFragment : AppBaseFragment(R.layout.fragment_signin), View.OnClickListener {
    private val binding by viewBinding(FragmentSigninBinding::bind)
    private val viewModel: LoginViewModel by viewModels()
    private var callbackManager: CallbackManager? = null

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
        binding.btnSignIn.setOnClickListener(this)
        binding.tvDontHaveAcocunt.setOnClickListener(this)
        binding.imgFacebook.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnSignIn -> {
                viewModel.isValidate(
                    binding.etxEmail.text!!.trim().toString(),
                    binding.etxPassword.text!!.trim().toString(),
                    resources.getString(R.string.str_false)
                )
            }
            binding.tvDontHaveAcocunt -> {
                sharedViewModel.setDirection(SignInFragmentDirections.navigateToSignup())
            }
            binding.imgFacebook -> {
                fbLogin()
            }
        }
    }

    private fun setObservers() {
        viewModel.users.observe(viewLifecycleOwner, {
            handleApiCallback(it)
        })
    }

    private fun handleApiCallback(apiResponse: Resource<Any>) {
        when (apiResponse.status) {
            Status.SUCCESS -> {
                this.hideProgress()
                when (apiResponse.apiConstant) {
                    ApiConstant.LOGIN -> {
                        val loginResponse = apiResponse.data as LoginResponse
                        showToast(requireActivity(), loginResponse.msg.toString())
                        preferences.setString(
                            AppConstants.USER_ID,
                            loginResponse.data!![0]!!.id.toString()
                        )
                        preferences.setString(
                            AppConstants.USER_IMAGE,
                            loginResponse.data[0]!!.image.toString()
                        )
                       // showProgressDialog(requireActivity())
                        prepareUser(loginResponse.data[0]!!.name!!)
                       /* startActivity(Intent(requireActivity(), HomeActivity::class.java))
                        requireActivity().finish()*/
                    }
                }
            }
            Status.LOADING -> {
                showProgress()
            }
            Status.ERROR -> {
                hideProgress()
                if(apiResponse.apiCode == 404){
                    val loginResponse = apiResponse.data as LoginResponse
                    showToast(requireContext(), loginResponse.msg!!)
                }else{
                    showToast(requireContext(), apiResponse.message!!)
                }
            }
            Status.RESOURCE -> {
                hideProgress()
                showToast(requireContext(), getString(apiResponse.resourceId!!))
            }
            else -> {

            }
        }
    }

    private fun fbLogin() {
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken == null || accessToken.isExpired)
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("public_profile", "email"))
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
                        this@SignInFragment.getString(R.string.err_facebook_authentication_fail)
                    )
                }

            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)

    }

    private fun setFacebookData(loginResult: LoginResult) {
        val request = GraphRequest.newMeRequest(
            loginResult.accessToken
        ) { _, response ->
            try {
                val loginRequest = LoginRequest()
                loginRequest.email =
                    response.jsonObject.getString(resources.getString(R.string.str_social_email))
                loginRequest.password = ""
                loginRequest.isSocial = resources.getString(R.string.str_true)
                val first_name = response.jsonObject.getString(
                    resources.getString(R.string.str_social_first_name)
                )
                loginRequest.name =
                    first_name + " " + response.jsonObject.getString(resources.getString(R.string.str_social_last_name))
                loginRequest.socialType = resources.getString(R.string.str_social_type)
                loginRequest.socialId = resources.getString(R.string.str_social_id)
                viewModel.authorizedUser(loginRequest)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,email,first_name,last_name")
        request.parameters = parameters
        request.executeAsync()
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
        qbUser.login = "vishavdeep.saini16@gmail.com"/*binding.etxEmail.text.toString().trim { it <= ' ' }*/
        qbUser.fullName = "vishav"
        qbUser.password = USER_DEFAULT_PASSWORD
        signIn(qbUser)
    }

    private fun signIn(user: QBUser) {
        showProgressDialog(requireActivity())
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

                startActivity(Intent(requireActivity(), HomeActivity::class.java))
                requireActivity().finish()
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