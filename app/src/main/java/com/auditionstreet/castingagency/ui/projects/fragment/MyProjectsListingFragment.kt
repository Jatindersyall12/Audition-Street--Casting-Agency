package com.auditionstreet.castingagency.ui.projects.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.auditionstreet.castingagency.BuildConfig
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentMyProjectsBinding
import com.auditionstreet.castingagency.model.response.MyProjectResponse
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.projects.adapter.MyProjectListAdapter
import com.auditionstreet.castingagency.ui.projects.viewmodel.MyProjectViewModel
import com.auditionstreet.castingagency.utils.AppConstants
import com.auditionstreet.castingagency.utils.showToast
import com.leo.wikireviews.utils.livedata.EventObserver
import com.silo.utils.AppBaseFragment
import com.silo.utils.network.Resource
import com.silo.utils.network.Status
import com.silo.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyProjectsListingFragment : AppBaseFragment(R.layout.fragment_my_projects),
    View.OnClickListener {
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
        setRecyclerViewListener()
    }

    private fun setRecyclerViewListener() {
        binding.rvProjects.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    binding.layAdd.visibility = View.GONE
                } else if (dy < 0) {
                    binding.layAdd.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun getMyProjects() {
        /* preferences.getString(
             AppConstants.USER_ID)*/
        viewModel.getMyProject(
            BuildConfig.BASE_URL + ApiConstant.GET_MY_PROJECTS + "/" + preferences.getString(
                AppConstants.USER_ID
            )
        )
    }

    private fun setListeners() {
        binding.btnAddProject.setOnClickListener(this)
        binding.layAdd.setOnClickListener(this)
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
            { projectId: String ->
                sharedViewModel.setDirection(
                    MyProjectsListingFragmentDirections.navigateToProjectDetail(
                        projectId
                    )
                )
            }
            adapter = myProjectListAdapter
        }
    }

    private fun setAdapter(projectResponse: MyProjectResponse) {
        if (projectResponse.data.size > 0) {
            myProjectListAdapter.submitList(projectResponse.data)
            binding.rvProjects.visibility = View.VISIBLE
            binding.layNoRecord.visibility = View.GONE
            binding.layAdd.visibility=View.VISIBLE
        } else {
            binding.layAdd.visibility=View.GONE
            binding.rvProjects.visibility = View.GONE
            binding.layNoRecord.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnAddProject -> {
                sharedViewModel.setDirection(MyProjectsListingFragmentDirections.navigateToAddProject())
            }
            binding.layAdd -> {
                sharedViewModel.setDirection(MyProjectsListingFragmentDirections.navigateToAddProject())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getMyProjects()
    }
}