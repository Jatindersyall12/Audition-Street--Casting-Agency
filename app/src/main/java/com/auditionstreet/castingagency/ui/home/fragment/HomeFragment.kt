package com.auditionstreet.castingagency.ui.home.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.auditionstreet.castingagency.BuildConfig
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentHomeBinding
import com.auditionstreet.castingagency.model.response.HomeApiResponse
import com.auditionstreet.castingagency.model.response.MyProjectResponse
import com.auditionstreet.castingagency.model.response.ProjectResponse
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.home.activity.AllApplicationActivity
import com.auditionstreet.castingagency.ui.home.activity.OtherUserProfileActivity
import com.auditionstreet.castingagency.ui.home.activity.ShortlistedActivity
import com.auditionstreet.castingagency.ui.home.adapter.ApplicationListAdapter
import com.auditionstreet.castingagency.ui.home.adapter.HomeShortListAdapter
import com.auditionstreet.castingagency.ui.home.adapter.ProjectListAdapter
import com.auditionstreet.castingagency.ui.home.viewmodel.HomeViewModel
import com.auditionstreet.castingagency.ui.home.viewmodel.ProjectViewModel
import com.auditionstreet.castingagency.ui.projects.fragment.MyProjectsListingFragmentDirections
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
class HomeFragment : AppBaseFragment(R.layout.fragment_home), View.OnClickListener {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var projectListAdapter: ProjectListAdapter
    private lateinit var applicationListAdapter: ApplicationListAdapter
    private lateinit var shortListAdapter: HomeShortListAdapter

    private val viewModel: ProjectViewModel by viewModels()
    private val viewModelHome: HomeViewModel by viewModels()
    private var projectList = ArrayList<MyProjectResponse.Data>()
    private var applicationList = ArrayList<HomeApiResponse.Data.Request>()
    private var shortListedList = ArrayList<HomeApiResponse.Data.Accept>()

    @Inject
    lateinit var preferences: Preferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
        init()
        getProjectList()
        getHomeScreenData()
    }

    private fun getProjectList(){
        viewModel.getMyProject(
            BuildConfig.BASE_URL + ApiConstant.GET_MY_PROJECTS + "/" + preferences.getString(
                AppConstants.USER_ID
            )
        )
    }

    private fun getHomeScreenData(){
        viewModelHome.getHomeScreenData(
            BuildConfig.BASE_URL + ApiConstant.GET_HOME_DATA + preferences.getString(
                AppConstants.USER_ID
            )
        )
    }

    private fun setListeners() {
        binding.tvShortListMore.setOnClickListener(this)
        binding.tvViewAllApplication.setOnClickListener(this)
    }


    private fun setObservers() {
        viewModel.getMyProjects.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
        viewModelHome.getHomeScreenData.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
    }

    private fun handleApiCallback(apiResponse: Resource<Any>) {
        when (apiResponse.status) {
            Status.SUCCESS -> {
                hideProgress()
                when (apiResponse.apiConstant) {
                    ApiConstant.GET_PROJECTS -> {
                       val projectResponse = apiResponse.data as MyProjectResponse
                        projectList = projectResponse.data
                        setAdapter(projectResponse)
                    }
                    ApiConstant.GET_HOME_DATA ->{
                        val homeScreenDetailResponse = apiResponse.data as HomeApiResponse
                        applicationList = homeScreenDetailResponse.data.requestList as ArrayList<HomeApiResponse.Data.Request>
                        shortListedList = homeScreenDetailResponse.data.acceptList as ArrayList<HomeApiResponse.Data.Accept>
                        setApplicationAdapter(homeScreenDetailResponse.data.requestList)
                        setShortListAdapter(homeScreenDetailResponse.data.acceptList)
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
                sharedViewModel.setDirection(
                    HomeFragmentDirections.navigateToProjectDetail(
                        projectList[position].id.toString()
                    )
                )
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
                AppConstants.APPLICATIONID = applicationList[position].id.toString()
                val i = Intent(requireActivity(), AllApplicationActivity::class.java)
              //  i.putExtra("applicationId", applicationList[position].id.toString())
                startActivity(i)
                /*val i = Intent(requireActivity(), OtherUserProfileActivity::class.java)
                startActivity(i)*/
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
                AppConstants.ARTISTID = shortListedList[position].artistId.toString()
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

    private fun setAdapter(projectResponse: MyProjectResponse) {
        if (projectResponse.data.size > 0) {
            projectListAdapter.submitList(projectResponse.data)
            binding.rvSlidingProject.visibility = View.VISIBLE
            binding.tvNoProjectFound.visibility = View.GONE
        } else {
            binding.rvSlidingProject.visibility = View.GONE
             binding.tvNoProjectFound.visibility = View.VISIBLE
        }
    }

    private fun setApplicationAdapter(applicationRequestList: List<HomeApiResponse.Data.Request>) {
        if (applicationRequestList.size > 0) {
            applicationListAdapter.submitList(applicationRequestList)
            binding.rvApplication.visibility = View.VISIBLE
            binding.tvNoAppFoundCurrent.visibility = View.GONE
        } else {
            binding.rvApplication.visibility = View.GONE
            binding.tvNoAppFoundCurrent.visibility = View.VISIBLE
        }

    }

    private fun setShortListAdapter(shorListedList: List<HomeApiResponse.Data.Accept>) {
        if (shorListedList.size > 0) {
            shortListAdapter.submitList(shorListedList)
            binding.rvShortlist.visibility = View.VISIBLE
            binding.tvNoAppFoundSortListed.visibility = View.GONE
        } else {
            binding.rvShortlist.visibility = View.GONE
             binding.tvNoAppFoundSortListed.visibility = View.VISIBLE
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
                AppConstants.APPLICATIONID = ""
                val i = Intent(requireActivity(), AllApplicationActivity::class.java)
                // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                //  requireActivity().finish()
            }
        }
    }
}