package com.auditionstreet.castingagency.ui.home.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentHomeBinding
import com.auditionstreet.castingagency.model.response.ProjectResponse
import com.auditionstreet.castingagency.ui.home.activity.AllApplicationActivity
import com.auditionstreet.castingagency.ui.home.activity.OtherUserProfileActivity
import com.auditionstreet.castingagency.ui.home.activity.ShortlistedActivity
import com.auditionstreet.castingagency.ui.home.adapter.ApplicationListAdapter
import com.auditionstreet.castingagency.ui.home.adapter.HomeShortListAdapter
import com.auditionstreet.castingagency.ui.home.adapter.ProjectListAdapter
import com.auditionstreet.castingagency.ui.home.viewmodel.ProjectViewModel
import com.auditionstreet.castingagency.utils.showToast
import com.leo.wikireviews.utils.livedata.EventObserver
import com.silo.utils.AppBaseFragment
import com.silo.utils.network.Resource
import com.silo.utils.network.Status
import com.silo.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : AppBaseFragment(R.layout.fragment_home), View.OnClickListener {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var projectListAdapter: ProjectListAdapter
    private lateinit var applicationListAdapter: ApplicationListAdapter
    private lateinit var shortListAdapter: HomeShortListAdapter

    private val viewModel: ProjectViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
        init()
    }

    private fun setListeners() {
        binding.tvShortListMore.setOnClickListener(this)
        binding.tvViewAllApplication.setOnClickListener(this)
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
                        setAdapter(apiResponse.data as ProjectResponse)
                        setApplicationAdapter(apiResponse.data)
                        setShortListAdapter(apiResponse.data)

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

    private fun init() {
        binding.rvSlidingProject.apply {
            layoutManager = LinearLayoutManager(activity)
            projectListAdapter = ProjectListAdapter(requireActivity())
            { position: Int ->
                Log.e("position", "" + position)
            }
            adapter = projectListAdapter
            binding.rvSlidingProject.setLayoutManager(
                LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )
        }

        binding.rvApplication.apply {
            layoutManager = LinearLayoutManager(activity)
            applicationListAdapter = ApplicationListAdapter(requireActivity())
            { position: Int ->
                val i = Intent(requireActivity(), OtherUserProfileActivity::class.java)
                startActivity(i)
            }
            adapter = applicationListAdapter
            binding.rvApplication.setLayoutManager(
                LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )
        }

        binding.rvShortlist.apply {
            layoutManager = LinearLayoutManager(activity)
            shortListAdapter = HomeShortListAdapter(requireActivity())
            { position: Int ->
                val i = Intent(requireActivity(), OtherUserProfileActivity::class.java)
                startActivity(i)
            }
            adapter = shortListAdapter
            binding.rvShortlist.setLayoutManager(
                LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )
        }
    }

    private fun setAdapter(projectResponse: ProjectResponse) {
        if (projectResponse.data.size > 0) {
            projectListAdapter.submitList(projectResponse.data)
            binding.rvSlidingProject.visibility = View.VISIBLE
            //binding.tvNoRecordFound.visibility = View.GONE
        } else {
            binding.rvSlidingProject.visibility = View.GONE
            // binding.tvNoRecordFound.visibility = View.VISIBLE
        }
    }

    private fun setApplicationAdapter(projectResponse: ProjectResponse) {
        if (projectResponse.data.size > 0) {
            applicationListAdapter.submitList(projectResponse.data)
            binding.rvApplication.visibility = View.VISIBLE
            //binding.tvNoRecordFound.visibility = View.GONE
        } else {
            binding.rvApplication.visibility = View.GONE
            // binding.tvNoRecordFound.visibility = View.VISIBLE
        }

    }

    private fun setShortListAdapter(projectResponse: ProjectResponse) {
        if (projectResponse.data.size > 0) {
            shortListAdapter.submitList(projectResponse.data)
            binding.rvShortlist.visibility = View.VISIBLE
            //binding.tvNoRecordFound.visibility = View.GONE
        } else {
            binding.rvShortlist.visibility = View.GONE
            // binding.tvNoRecordFound.visibility = View.VISIBLE
        }

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tvShortListMore -> {
                val i = Intent(requireActivity(), ShortlistedActivity::class.java)
                // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                // requireActivity().finish()
            }
            R.id.tvViewAllApplication -> {
                val i = Intent(requireActivity(), AllApplicationActivity::class.java)
                // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                //  requireActivity().finish()
            }
        }
    }
}