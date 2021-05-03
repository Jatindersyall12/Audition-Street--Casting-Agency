package com.auditionstreet.castingagency.ui.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentOtherUserBinding
import com.auditionstreet.castingagency.model.response.ProjectResponse
import com.auditionstreet.castingagency.ui.home.adapter.ShortListAdapter
import com.auditionstreet.castingagency.ui.home.viewmodel.ProjectViewModel
import com.auditionstreet.castingagency.utils.showToast
import com.leo.wikireviews.utils.livedata.EventObserver
import com.silo.utils.AppBaseFragment
import com.silo.utils.network.Resource
import com.silo.utils.network.Status
import com.silo.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherUserProfileFragment : AppBaseFragment(R.layout.fragment_other_user) {
    private val binding by viewBinding(FragmentOtherUserBinding::bind)
    private lateinit var shortListAdapter: ShortListAdapter
    private val viewModel: ProjectViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
       // init()
    }

    private fun setListeners() {
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
                    ApiConstant.GET_PROJECTS -> {
                       // setAdapter(apiResponse.data as ProjectResponse)
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
        binding.rvShortList.apply {
            layoutManager = LinearLayoutManager(activity)
            shortListAdapter = ShortListAdapter(requireActivity())
            { position: Int ->
                Log.e("position", "" + position)
            }
            adapter = shortListAdapter
        }
    }
    private fun setAdapter(projectResponse: ProjectResponse) {
        if (projectResponse.data.size > 0) {
            shortListAdapter.submitList(projectResponse.data)
            binding.rvShortList.visibility = View.VISIBLE
            //binding.tvNoRecordFound.visibility = View.GONE
        } else {
            binding.rvShortList.visibility = View.GONE
           // binding.tvNoRecordFound.visibility = View.VISIBLE
        }
    }*/
}