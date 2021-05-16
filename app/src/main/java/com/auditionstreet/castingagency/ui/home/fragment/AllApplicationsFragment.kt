package com.auditionstreet.castingagency.ui.home.fragment

import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import com.auditionstreet.castingagency.BuildConfig
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentAllApplicationsBinding
import com.auditionstreet.castingagency.model.response.MyProjectResponse
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.home.adapter.AllApplicationsAdapter
import com.auditionstreet.castingagency.ui.home.viewmodel.ProjectViewModel
import com.auditionstreet.castingagency.utils.AppConstants
import com.auditionstreet.castingagency.utils.showSelectProjectDialog
import com.auditionstreet.castingagency.utils.showToast
import com.leo.wikireviews.utils.livedata.EventObserver
import com.silo.utils.AppBaseFragment
import com.silo.utils.network.Resource
import com.silo.utils.network.Status
import com.silo.utils.viewbinding.viewBinding
import com.yuyakaido.android.cardstackview.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AllApplicationsFragment : AppBaseFragment(R.layout.fragment_all_applications),
    CardStackListener, View.OnClickListener {
    private val binding by viewBinding(FragmentAllApplicationsBinding::bind)
    private lateinit var allApplicationsAdapter: AllApplicationsAdapter
    private val manager by lazy { CardStackLayoutManager(requireActivity(), this) }
    private val viewModel: ProjectViewModel by viewModels()
    private lateinit var projectList: MyProjectResponse;

    @Inject
    lateinit var preferences: Preferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
        init()
        getAllApplications()
    }

    private fun setListeners() {
        binding.tvSelectProject.setOnClickListener(this)
    }

    private fun getAllApplications() {
        viewModel.getAllApplications(
            BuildConfig.BASE_URL + ApiConstant.GET_MY_PROJECTS + "/" + preferences.getString(
                AppConstants.USER_ID
            )
        )
    }

    private fun setObservers() {
        viewModel.allAppliactions.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })

    }

    private fun handleApiCallback(apiResponse: Resource<Any>) {
        when (apiResponse.status) {
            Status.SUCCESS -> {
                hideProgress()
                when (apiResponse.apiConstant) {
                    ApiConstant.GET_MY_PROJECTS -> {
                        projectList = apiResponse.data as MyProjectResponse
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
            else -> {

            }
        }
    }

    private fun init() {
        binding.cardAllApplications.apply {
            allApplicationsAdapter = AllApplicationsAdapter(requireActivity())
            { position: Int ->
                projectList.data.removeAt(0)
                //if (position == 0)
                allApplicationsAdapter.submitList(projectList.data)
                // else
                //   binding.cardAllApplications.rewind()
            }
            adapter = allApplicationsAdapter
            manager.setStackFrom(StackFrom.None)
            manager.setVisibleCount(1)
            manager.setTranslationInterval(12.0f)
            manager.setScaleInterval(0.95f)
            manager.setSwipeThreshold(0.3f)
            manager.setMaxDegree(80.0f)
            manager.setDirections(Direction.HORIZONTAL)
            manager.setCanScrollHorizontal(true)
            manager.setCanScrollVertical(true)
            manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
            manager.setOverlayInterpolator(LinearInterpolator())
            binding.cardAllApplications.layoutManager = manager
            binding.cardAllApplications.adapter = allApplicationsAdapter
            binding.cardAllApplications.itemAnimator.apply {
                if (this is DefaultItemAnimator) {
                    supportsChangeAnimations = false
                }
            }
        }
    }

    private fun setAdapter(projectResponse: MyProjectResponse) {
        if (projectResponse.data.size > 0) {
            allApplicationsAdapter.submitList(projectResponse.data)
            // binding.rvShortList.visibility = View.VISIBLE
            //binding.tvNoRecordFound.visibility = View.GONE
        } else {
            // binding.rvShortList.visibility = View.GONE
            // binding.tvNoRecordFound.visibility = View.VISIBLE
        }
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
        if (direction!!.name.equals("Left"))
            showToast(requireActivity(), "Rejected")
        else
            showToast(requireActivity(), "Accpeted")
    }

    override fun onCardRewound() {
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tvSelectProject -> {
                showSelectProject()
            }
        }
    }

    private fun showSelectProject() {
        showSelectProjectDialog(requireActivity(), projectList)
        {

        }
    }
}
