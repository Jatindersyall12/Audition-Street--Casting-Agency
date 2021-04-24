package com.auditionstreet.castingagency.ui.projects.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentMyProjectsBinding
import com.auditionstreet.castingagency.model.response.MyProjectResponse
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.login_signup.fragment.SignInFragmentDirections
import com.auditionstreet.castingagency.ui.projects.adapter.MyProjectListAdapter
import com.auditionstreet.castingagency.ui.projects.viewmodel.MyProjectViewModel
import com.auditionstreet.castingagency.utils.AppConstants
import com.auditionstreet.castingagency.utils.showToast
import com.leo.wikireviews.utils.livedata.EventObserver
import com.silo.model.request.MyProjectRequest
import com.silo.utils.AppBaseFragment
import com.silo.utils.network.Resource
import com.silo.utils.network.Status
import com.silo.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyProjectsListingFragment : AppBaseFragment(R.layout.fragment_my_projects),View.OnClickListener {
    private val binding by viewBinding(FragmentMyProjectsBinding::bind)
    private lateinit var myProjectListAdapter: MyProjectListAdapter

    private val viewModel: MyProjectViewModel by viewModels()

    @Inject
    lateinit var preferences: Preferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
        init()
        getMyProjects()
    }

    private fun getMyProjects() {
        var request=MyProjectRequest()
        request.userid=preferences.getString(AppConstants.USER_ID)
        viewModel.getMyProject(request)
    }

    private fun setListeners() {
        binding.btnAddProject.setOnClickListener(this)
    }


    private fun setObservers() {
        viewModel.users.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
    }

    private fun handleApiCallback(apiResponse: Resource<Any>) {
        when (apiResponse.status) {
            Status.SUCCESS -> {
                hideProgress()
                when (apiResponse.apiConstant) {
                    ApiConstant.GET_MY_PROJECTS -> {
                        setAdapter(apiResponse.data as MyProjectResponse)
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

    private fun init() {
        binding.rvProjects.apply {
            layoutManager = LinearLayoutManager(activity)
            myProjectListAdapter = MyProjectListAdapter(requireActivity())
            { position: Int ->
                Log.e("position", "" + position)
            }
            adapter = myProjectListAdapter
        }
    }

    private fun setAdapter(projectResponse: MyProjectResponse) {
        if (projectResponse.data.size > 0) {
            myProjectListAdapter.submitList(projectResponse.data)
            binding.rvProjects.visibility = View.VISIBLE
            //binding.tvNoRecordFound.visibility = View.GONE
        } else {
            binding.rvProjects.visibility = View.GONE
            // binding.tvNoRecordFound.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnAddProject -> {
                sharedViewModel.setDirection(SignInFragmentDirections.navigateToSignup())

            }
        }
    }
}