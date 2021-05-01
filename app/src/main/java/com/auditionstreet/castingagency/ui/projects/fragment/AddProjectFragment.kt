package com.auditionstreet.castingagency.ui.projects.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.auditionstreet.castingagency.BuildConfig
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentAddProjectBinding
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.projects.viewmodel.AddProjectViewModel
import com.auditionstreet.castingagency.utils.showToast
import com.leo.wikireviews.utils.livedata.EventObserver
import com.silo.utils.AppBaseFragment
import com.silo.utils.network.Resource
import com.silo.utils.network.Status
import com.silo.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddProjectFragment : AppBaseFragment(R.layout.fragment_add_project), View.OnClickListener{
    private val binding by viewBinding(FragmentAddProjectBinding::bind)

    private val viewModel: AddProjectViewModel by viewModels()

    @Inject
    lateinit var preferences: Preferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
        getAllUsers()
        setSeekBarListsener()
    }

    private fun setSeekBarListsener() {
        binding.rangeSeekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            binding.tvAge.text =
                resources.getString(R.string.str_age_range) + minValue.toString() + "-" + maxValue.toString()
        }
        binding.rangeSeekbar.setMinValue(18f).setMaxValue(50f).setMinStartValue(25f)
            .setMaxStartValue(40f)
            .apply()
    }

    private fun getAllUsers() {
        viewModel.getAllUsers(
            BuildConfig.BASE_URL + ApiConstant.GET_ALL_USERS
        )
    }

    private fun setListeners() {
        // binding.btnAddProject.setOnClickListener(this)
    }


    private fun setObservers() {
        viewModel.allUsers.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
    }

    private fun handleApiCallback(apiResponse: Resource<Any>) {
        when (apiResponse.status) {
            Status.SUCCESS -> {
                hideProgress()
                when (apiResponse.apiConstant) {
                    ApiConstant.GET_MY_PROJECTS -> {
                        // setAdapter(apiResponse.data as MyProjectResponse)
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

    /*private fun init() {
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
    }*/

    override fun onClick(v: View?) {
        when (v) {
            //   binding.btnAddProject -> {

        }
    }
    }
