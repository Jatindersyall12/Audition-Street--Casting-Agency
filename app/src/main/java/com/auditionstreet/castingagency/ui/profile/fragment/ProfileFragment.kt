package com.auditionstreet.castingagency.ui.projects.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentProfileBinding
import com.auditionstreet.castingagency.model.response.ProfileResponse
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.profile.viewmodel.ProfileViewModel
import com.auditionstreet.castingagency.ui.projects.adapter.WorkListAdapter
import com.auditionstreet.castingagency.utils.showToast
import com.leo.wikireviews.utils.livedata.EventObserver
import com.silo.utils.AppBaseFragment
import com.silo.utils.network.Resource
import com.silo.utils.network.Status
import com.silo.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : AppBaseFragment(R.layout.fragment_profile), View.OnClickListener {
    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var profileAdapter: WorkListAdapter

    @Inject
    lateinit var preferences: Preferences
    private lateinit var profileResponse: ProfileResponse


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
        getUserProfile()
        init()
    }

    private fun getUserProfile() {
        /*viewModel.getProfile(
            BuildConfig.BASE_URL + ApiConstant.GET_PROFILE
        )*/
    }

    private fun setListeners() {
        binding.tvViewAll.setOnClickListener(this)

    }


    private fun setObservers() {
        viewModel.getProfile.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })

    }

    private fun handleApiCallback(apiResponse: Resource<Any>) {
        when (apiResponse.status) {
            Status.SUCCESS -> {
                hideProgress()
                when (apiResponse.apiConstant) {
                    ApiConstant.GET_PROFILE -> {
                        profileResponse = apiResponse.data as ProfileResponse
                        setWorkAdapter(profileResponse)
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

    private fun setWorkAdapter(profileResponse: ProfileResponse) {
        if (profileResponse.data.size > 0) {
            profileAdapter.submitList(profileResponse.data)
            binding.rvWork.visibility = View.VISIBLE
            // binding.layNoRecord.visibility = View.GONE
        } else {
            binding.rvWork.visibility = View.GONE
            //  binding.layNoRecord.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvViewAll -> {
                showToast(requireActivity(), resources.getString(R.string.str_coming_soon))
            }
        }
    }

    private fun init() {
        binding.rvWork.apply {
            layoutManager = LinearLayoutManager(activity)
            profileAdapter = WorkListAdapter(requireActivity())
            { projectId: String ->
                sharedViewModel.setDirection(
                    MyProjectsListingFragmentDirections.navigateToProjectDetail(
                        projectId
                    )
                )
            }
            adapter = profileAdapter
            binding.rvWork.setLayoutManager(
                LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )
        }
    }
}
