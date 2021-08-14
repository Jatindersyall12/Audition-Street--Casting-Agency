package com.auditionstreet.castingagency.ui.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import com.auditionstreet.castingagency.BuildConfig
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentAllApplicationsBinding
import com.auditionstreet.castingagency.model.response.ApplicationResponse
import com.auditionstreet.castingagency.model.response.MyProjectResponse
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.home.adapter.AllApplicationsAdapter
import com.auditionstreet.castingagency.ui.home.viewmodel.ProjectViewModel
import com.auditionstreet.castingagency.utils.AppConstants
import com.auditionstreet.castingagency.utils.showSelectProjectDialog
import com.auditionstreet.castingagency.utils.showToast
import com.leo.wikireviews.utils.livedata.EventObserver
import com.silo.model.request.AcceptRejectArtistRequest
import com.silo.model.request.BlockArtistRequest
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
    private lateinit var applicationListResponse: ApplicationResponse;
    private lateinit var projectListResponse: MyProjectResponse
    private var seletcedAplicationList: ArrayList<ApplicationResponse.Data> ?= null
    private var cardCurrentPosition = 0
    private var applicationId = ""

    @Inject
    lateinit var preferences: Preferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seletcedAplicationList = ArrayList()
        applicationId = AppConstants.APPLICATIONID
        setListeners()
        setObservers()
        init()
        getAllApplications()
        getAllMyProjects()
    }

    private fun setListeners() {
        binding.tvSelectProject.setOnClickListener(this)
    }

    private fun getAllApplications() {
        viewModel.getAllApplications(
            BuildConfig.BASE_URL + ApiConstant.GET_REQUEST_APPLICATIONS + "/" + preferences.getString(
                AppConstants.USER_ID
            )
        )
    }

    private fun getAllMyProjects() {
        viewModel.getMyProject(
            BuildConfig.BASE_URL + ApiConstant.GET_MY_PROJECTS + "/" + preferences.getString(
                AppConstants.USER_ID
            )
        )
    }

    private fun setObservers() {
        viewModel.allAppliactions.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
        viewModel.getMyProjects.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
        viewModel.acceptRejectArtist.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
        viewModel.blockArtist.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })


    }

    private fun handleApiCallback(apiResponse: Resource<Any>) {
        when (apiResponse.status) {
            Status.SUCCESS -> {
                hideProgress()
                when (apiResponse.apiConstant) {
                    ApiConstant.GET_REQUEST_APPLICATIONS -> {
                        seletcedAplicationList!!.clear()
                            applicationListResponse = apiResponse.data as ApplicationResponse
                        setAdapter(applicationListResponse)
                        if (!applicationId.isEmpty()){
                            binding.tvSelectProject.visibility = View.GONE
                            for (i in 0 until applicationListResponse.data.size){
                                if (applicationListResponse.data[i].id == applicationId.toInt()){
                                    seletcedAplicationList!!.add(applicationListResponse.data[i])
                                }
                            }
                        }
                    }
                    ApiConstant.GET_MY_PROJECTS -> {
                            projectListResponse = apiResponse.data as MyProjectResponse
                    }
                    ApiConstant.ACCEPT_REJECT_ARTIST -> {
                        applicationListResponse.data.removeAt(0)
                        allApplicationsAdapter.submitList(applicationListResponse.data)
                    }
                    ApiConstant.BLOCK_ARTIST -> {
                        val acceptRejectArtistRequest = AcceptRejectArtistRequest()
                        acceptRejectArtistRequest.castingId = preferences.getString(AppConstants.USER_ID)
                        acceptRejectArtistRequest.projectId = applicationListResponse.data[cardCurrentPosition].projectId
                        acceptRejectArtistRequest.id = applicationListResponse.data[cardCurrentPosition].id.toString()
                        acceptRejectArtistRequest.status = "2"
                        acceptRejectArtistRequest.userStatus = "2"
                        viewModel.acceptRejectArtist(acceptRejectArtistRequest)
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
                if (position == 0){
                    val acceptRejectArtistRequest = AcceptRejectArtistRequest()
                    acceptRejectArtistRequest.castingId = preferences.getString(AppConstants.USER_ID)
                    acceptRejectArtistRequest.projectId = applicationListResponse.data[cardCurrentPosition].projectId
                    acceptRejectArtistRequest.id = applicationListResponse.data[cardCurrentPosition].id.toString()
                    acceptRejectArtistRequest.status = "1"
                    acceptRejectArtistRequest.userStatus = "2"
                    viewModel.acceptRejectArtist(acceptRejectArtistRequest)
                }else if(position == 1){
                    val blockArtistRequest = BlockArtistRequest()
                    blockArtistRequest.artistId = applicationListResponse.data[cardCurrentPosition].artistId
                    blockArtistRequest.castingId = applicationListResponse.data[cardCurrentPosition].castingId
                    viewModel.blockArtist(blockArtistRequest)
                }

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

    private fun setAdapter(projectResponse: ApplicationResponse) {
        if (projectResponse.data.size > 0) {
            allApplicationsAdapter.submitList(projectResponse.data)
            binding.cardAllApplications.visibility = View.VISIBLE
            binding.tvNoAppFound.visibility = View.GONE
        } else {
            binding.cardAllApplications.visibility = View.GONE
            binding.tvNoAppFound.visibility = View.VISIBLE
        }
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
        if (direction!!.name.equals("Left")) {
            val acceptRejectArtistRequest = AcceptRejectArtistRequest()
            acceptRejectArtistRequest.castingId = preferences.getString(AppConstants.USER_ID)
            acceptRejectArtistRequest.projectId = applicationListResponse.data[cardCurrentPosition].projectId
            acceptRejectArtistRequest.id = applicationListResponse.data[cardCurrentPosition].id.toString()
            acceptRejectArtistRequest.status = "2"
            acceptRejectArtistRequest.userStatus = "2"
            viewModel.acceptRejectArtist(acceptRejectArtistRequest)
            showToast(requireActivity(), "Rejected")
        }
        else {
            val acceptRejectArtistRequest = AcceptRejectArtistRequest()
            acceptRejectArtistRequest.castingId = preferences.getString(AppConstants.USER_ID)
            acceptRejectArtistRequest.projectId = applicationListResponse.data[cardCurrentPosition].projectId
            acceptRejectArtistRequest.id = applicationListResponse.data[cardCurrentPosition].id.toString()
            acceptRejectArtistRequest.status = "1"
            acceptRejectArtistRequest.userStatus = "2"
            viewModel.acceptRejectArtist(acceptRejectArtistRequest)
            showToast(requireActivity(), "Accpeted")
        }
    }

    override fun onCardRewound() {
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
        cardCurrentPosition = position
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
        showSelectProjectDialog(requireActivity(), projectListResponse)
        { projectId: String ->
            seletcedAplicationList!!.clear()
            for (i in 0 until applicationListResponse.data.size){
                if (applicationListResponse.data[i].projectId == projectId){
                    seletcedAplicationList!!.add(applicationListResponse.data[i])
                }
            }
            if (!seletcedAplicationList.isNullOrEmpty()){
                allApplicationsAdapter.submitList(seletcedAplicationList!!)
                binding.cardAllApplications.visibility = View.VISIBLE
                binding.tvNoAppFound.visibility = View.GONE
            }else{
                binding.cardAllApplications.visibility = View.GONE
                binding.tvNoAppFound.visibility = View.VISIBLE
            }
        }
    }
}
