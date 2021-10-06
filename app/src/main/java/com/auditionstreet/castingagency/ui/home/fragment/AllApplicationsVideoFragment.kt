package com.auditionstreet.castingagency.ui.home.fragment

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.auditionstreet.castingagency.BuildConfig
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentAllApplicationsBinding
import com.auditionstreet.castingagency.databinding.FragmentAllApplicationsVideoBinding
import com.auditionstreet.castingagency.model.response.ApplicationResponse
import com.auditionstreet.castingagency.model.response.MyProjectResponse
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.home.activity.AllApplicationActivity
import com.auditionstreet.castingagency.ui.home.activity.OtherUserProfileActivity
import com.auditionstreet.castingagency.ui.home.adapter.AllApplicationsAdapter
import com.auditionstreet.castingagency.ui.home.viewmodel.ProjectViewModel
import com.auditionstreet.castingagency.utils.*
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.leo.wikireviews.utils.livedata.EventObserver
import com.silo.model.request.AcceptRejectArtistRequest
import com.silo.utils.AppBaseFragment
import com.silo.utils.network.Resource
import com.silo.utils.network.Status
import com.silo.utils.viewbinding.viewBinding
import com.yuyakaido.android.cardstackview.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.all_application_item.view.*
import javax.inject.Inject

@AndroidEntryPoint
class AllApplicationsVideoFragment : AppBaseFragment(R.layout.fragment_all_applications_video), View.OnClickListener {
    private val binding by viewBinding(FragmentAllApplicationsVideoBinding::bind)
    private val viewModel: ProjectViewModel by viewModels()
    private var videoUrl = ""
    private var projectId = ""
    private var artistId = ""

    @Inject
    lateinit var preferences: Preferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireArguments().getString("projectId") != null) {
            videoUrl =
                requireArguments().getString("videoUrl").toString()
            projectId =
                requireArguments().getString("projectId").toString()
            artistId =
                requireArguments().getString("artistId").toString()
        }
        playActorsVideo()
        setListener()
        setObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        //stopVideo()
    }

    override fun onPause() {
        super.onPause()
        stopVideo()
    }

    private fun setObservers() {
        viewModel.acceptRejectArtist.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
    }

    private fun handleApiCallback(apiResponse: Resource<Any>) {
        when (apiResponse.status) {
            Status.SUCCESS -> {
                hideProgress()
                when (apiResponse.apiConstant) {
                    ApiConstant.ACCEPT_REJECT_ARTIST -> {
                       findNavController().popBackStack()
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

    private fun setListener(){
        binding.ivBack.setOnClickListener(this)
        binding.ivAccept.setOnClickListener(this)
        binding.ivReject.setOnClickListener(this)
        binding.playerView.setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
    }

    private fun playActorsVideo() {
        if (!videoUrl.isNullOrEmpty()) {
            playVideo(
                requireActivity(), binding.playerView,
                videoUrl

            )
        }
    }

    private fun stopVideo(){
        if (binding.playerView.player != null) {
            binding.playerView.player!!.playWhenReady = false
            binding.playerView.player!!.stop()
            binding.playerView.player!!.release()
            binding.playerView.player = null

        }
    }



    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ivBack ->{
                findNavController().popBackStack()
            }
            R.id.ivAccept ->{
                val acceptRejectArtistRequest = AcceptRejectArtistRequest()
                acceptRejectArtistRequest.castingId = preferences.getString(AppConstants.USER_ID)
                acceptRejectArtistRequest.projectId = projectId
                acceptRejectArtistRequest.id = artistId
                acceptRejectArtistRequest.status = "1"
                acceptRejectArtistRequest.userStatus = "2"
                viewModel.acceptRejectArtist(acceptRejectArtistRequest)
                showToast(requireActivity(), "Profile Accepted")
            }
            R.id.ivReject ->{
                val acceptRejectArtistRequest = AcceptRejectArtistRequest()
                acceptRejectArtistRequest.castingId = preferences.getString(AppConstants.USER_ID)
                acceptRejectArtistRequest.projectId = projectId
                acceptRejectArtistRequest.id = artistId
                acceptRejectArtistRequest.status = "2"
                acceptRejectArtistRequest.userStatus = "2"
                viewModel.acceptRejectArtist(acceptRejectArtistRequest)
                showToast(requireActivity(), "Profile Rejected")
            }

        }
    }
}
