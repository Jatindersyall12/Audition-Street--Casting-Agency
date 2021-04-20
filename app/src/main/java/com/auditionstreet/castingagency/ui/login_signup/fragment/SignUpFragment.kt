package com.auditionstreet.castingagency.ui.login_signup.fragment

import android.os.Bundle
import android.view.View
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.databinding.FragmentSignupBinding
import com.silo.utils.AppBaseFragment
import com.silo.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : AppBaseFragment(R.layout.fragment_signup), View.OnClickListener {
    private val binding by viewBinding(FragmentSignupBinding::bind)

    // private val viewModel: LoginViewModel by viewModels()
    private var isPasswordVisible: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*setListeners()
        setObservers()
        enableButtonClick(0.3f, false)*/
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    /*private fun setListeners() {
        binding.txtUserName.addTextChangedListener(TextChangeWatcher(binding.txtUserName))
        binding.txtPassword.addTextChangedListener(TextChangeWatcher(binding.txtPassword))
        binding.imgShowPassword.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        binding.txtSignup.setOnClickListener(this)
        binding.forgotPassword.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnLogin -> {
                viewModel.apiLogin()
            }

            binding.txtSignup -> {
                sharedViewModel.setDirection(LoginFragmentDirections.navigateToSignup())
            }
            binding.forgotPassword -> {
                sharedViewModel.setDirection(LoginFragmentDirections.navigateToForgotPassword())
            }
            binding.imgShowPassword -> {
                if (!isPasswordVisible) {
                    isPasswordVisible = true
                    binding.imgShowPassword.setImageResource(R.drawable.ic_show_password)
                    binding.txtPassword.transformationMethod = null
                } else {
                    isPasswordVisible = false
                    binding.imgShowPassword.setImageResource(R.drawable.ic_hide_password)
                    binding.txtPassword.transformationMethod = PasswordTransformationMethod()
                }
                binding.txtPassword.setSelection(binding.txtPassword.text!!.length)
            }
        }
    }

    private fun setObservers() {
        viewModel.userAuthenticationApiResponse.observe(viewLifecycleOwner, {
            handleApiCallback(it)
        })
    }

    private fun handleApiCallback(apiResponse: Resource<Any>) {
        when (apiResponse.status) {
            Status.SUCCESS -> {
                hideProgress()
                when (apiResponse.apiConstant) {
                    LOGIN -> {
                        // val response = apiResponse.data as LoginResponse
                        startActivity(Intent(requireContext(), CreateProfileActivity::class.java))
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
            Status.ALPHA -> {
                if (getString(apiResponse.resourceId!!).equals(
                        getString(R.string.alpha_true),
                        true
                    )
                ) {
                    enableButtonClick(1.0f, true)
                } else enableButtonClick(0.3f, false)
            }
        }
    }

    private fun enableButtonClick(alpha: Float, clickable: Boolean) {
        binding.btnLogin.isEnabled = clickable
        binding.btnLogin.alpha = alpha
    }*/
}