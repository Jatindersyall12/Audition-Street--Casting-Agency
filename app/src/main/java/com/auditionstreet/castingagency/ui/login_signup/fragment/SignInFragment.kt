package com.auditionstreet.castingagency.ui.login_signup.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.databinding.FragmentSigninBinding
import com.auditionstreet.castingagency.ui.login_signup.viewmodel.LoginViewModel
import com.silo.utils.AppBaseFragment
import com.silo.utils.network.Resource
import com.silo.utils.network.Status
import com.silo.utils.showToast
import com.silo.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : AppBaseFragment(R.layout.fragment_signin), View.OnClickListener {
    private val binding by viewBinding(FragmentSigninBinding::bind)

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
    }


    private fun setListeners() {
        binding.btnSignIn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnSignIn -> {
                viewModel.isValidate(
                    binding.etxEmail.text!!.trim().toString(),
                    binding.etxPassword.text!!.trim().toString()
                )
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
}