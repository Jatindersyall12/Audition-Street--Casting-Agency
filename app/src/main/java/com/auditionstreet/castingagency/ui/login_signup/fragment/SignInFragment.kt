package com.auditionstreet.castingagency.ui.login_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentSigninBinding
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.home.activity.HomeActivity
import com.auditionstreet.castingagency.ui.login_signup.viewmodel.LoginViewModel
import com.auditionstreet.castingagency.utils.AppConstants
import com.auditionstreet.castingagency.utils.showToast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.silo.model.request.LoginRequest
import com.silo.model.response.LoginResponse
import com.silo.utils.AppBaseFragment
import com.silo.utils.network.Resource
import com.silo.utils.network.Status
import com.silo.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import javax.inject.Inject

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
                        startActivity(Intent(requireActivity(), HomeActivity::class.java))
                        requireActivity().finish()
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
}