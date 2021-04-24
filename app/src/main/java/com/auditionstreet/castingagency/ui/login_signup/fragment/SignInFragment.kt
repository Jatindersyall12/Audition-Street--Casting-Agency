package com.auditionstreet.castingagency.ui.login_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentSigninBinding
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.home.activity.HomeActivity
import com.auditionstreet.castingagency.ui.login_signup.AuthorizedUserActivity
import com.auditionstreet.castingagency.ui.login_signup.viewmodel.LoginViewModel
import com.auditionstreet.castingagency.utils.AppConstants
import com.auditionstreet.castingagency.utils.showToast
import com.facebook.*
import com.facebook.appevents.AppEventsConstants
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
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
                    binding.etxPassword.text!!.trim().toString()
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
                hideProgress()
                when (apiResponse.apiConstant) {
                    ApiConstant.LOGIN -> {
                        val loginResponse = apiResponse.data as LoginResponse
                        showToast(requireActivity(), loginResponse.msg)
                        preferences.setString(
                            AppConstants.USER_ID,
                            loginResponse.data[0].id.toString()
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
                    /*showToast(
                        requireActivity(),
                        getString(R.string.err_facebook_authentication_fail)
                    )*/
                }

                override fun onError(error: FacebookException) {
                    Log.e("error", error.message.toString())
                    /*showToast(
                        this@SocialLoginActivity,
                        getString(R.string.err_facebook_authentication_fail)
                    )*/
                }

            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)

    }

    private fun setFacebookData(loginResult: LoginResult) {
        val request = GraphRequest.newMeRequest(
            loginResult.accessToken
        ) { `object`, response ->
            try {
                Log.e("resultttt", response.jsonObject.getString("id"))
                /*viewModel.socialSignIn(
                    viewModel.getSocialSignInRequest(
                        response.jsonObject.getString(
                            "email"
                        ),
                        response.jsonObject.getString("id"),
                        resources.getString(R.string.facebook)
                    )
                )*/
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