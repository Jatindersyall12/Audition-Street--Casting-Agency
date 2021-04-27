package com.auditionstreet.castingagency.ui.projects.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.auditionstreet.castingagency.BuildConfig
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentMyProjectDetailBinding
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.projects.viewmodel.MyProjectDetailViewModel
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
class MyProjectDetailFragment : AppBaseFragment(R.layout.fragment_my_project_detail),
    View.OnClickListener {
    private val binding by viewBinding(FragmentMyProjectDetailBinding::bind)

    private val viewModel: MyProjectDetailViewModel by viewModels()

    @Inject
    lateinit var preferences: Preferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
        getMyProjectDetail()
    }

    private fun getMyProjectDetail() {
        /* preferences.getString(
            AppConstants.USER_ID)*/
        viewModel.getMyProjectDetail(
            BuildConfig.BASE_URL + ApiConstant.GET_MY_PROJECTS_DETAILS + "/" + "1"
        )
    }

    private fun setListeners() {
        // binding.btnAddProject.setOnClickListener(this)
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
                    ApiConstant.GET_MY_PROJECTS_DETAILS -> {
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

    override fun onClick(v: View?) {
        when (v) {
            //   binding.btnAddProject -> {

        }
    }
    //  }
}