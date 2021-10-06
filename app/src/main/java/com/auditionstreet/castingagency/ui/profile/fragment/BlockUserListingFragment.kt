package com.auditionstreet.castingagency.ui.profile.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.auditionstreet.castingagency.BuildConfig
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentBlockUserListingBinding
import com.auditionstreet.castingagency.model.response.AddGroupResponse
import com.auditionstreet.castingagency.model.response.BlockUserListResponse
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.profile.adapter.BlockedUserListAdapter
import com.auditionstreet.castingagency.ui.profile.viewmodel.BlockUserViewModel
import com.auditionstreet.castingagency.ui.projects.viewmodel.MyProjectViewModel
import com.auditionstreet.castingagency.utils.AppConstants
import com.auditionstreet.castingagency.utils.showToast
import com.leo.wikireviews.utils.livedata.EventObserver
import com.silo.model.request.BlockArtistRequest
import com.silo.utils.AppBaseFragment
import com.silo.utils.network.Resource
import com.silo.utils.network.Status
import com.silo.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class BlockUserListingFragment : AppBaseFragment(R.layout.fragment_block_user_listing),
    View.OnClickListener {
    private val binding by viewBinding(FragmentBlockUserListingBinding::bind)
    private lateinit var blockedUserListAdapter: BlockedUserListAdapter

    private val viewModel: BlockUserViewModel by viewModels()
    private var blockedUserList : ArrayList<BlockUserListResponse.Data> ?= null
    private var blockUserSelectedPosition = 0

    @Inject
    lateinit var preferences: Preferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        blockedUserList = ArrayList()

        setListeners()
        setObservers()
        init()
    }

    private fun getBlockUserList() {
        viewModel.getBlockUserList(
            BuildConfig.BASE_URL + ApiConstant.GET_BLOCK_ARTIST + "/" + preferences.getString(
                AppConstants.USER_ID
            )
        )
    }

    private fun setListeners() {
        binding.imgBack.setOnClickListener(this)
    }


    private fun setObservers() {
        viewModel.getBlockUserList.observe(viewLifecycleOwner, EventObserver {
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
                    ApiConstant.GET_BLOCK_ARTIST -> {
                        val response = apiResponse.data as BlockUserListResponse
                        blockedUserList =  response.data
                        setAdapter(blockedUserList!!)
                    }
                    ApiConstant.BLOCK_ARTIST -> {
                        getBlockUserList()
                        showToast(requireActivity(), "Unblocked Successfully")
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
        binding.rvBlockUser.apply {
            layoutManager = LinearLayoutManager(activity)
            blockedUserListAdapter = BlockedUserListAdapter(requireActivity())
            { position: String ->
                blockUserSelectedPosition = position.toInt()
                val blockArtistRequest = BlockArtistRequest()
                blockArtistRequest.artistId = blockedUserList!![position.toInt()].artistId
                blockArtistRequest.castingId =  preferences.getString(AppConstants.USER_ID)
                // Status 1 = Block, 2 = Unblock
                blockArtistRequest.status = "2"
                viewModel.blockArtist(blockArtistRequest)
            }
            adapter = blockedUserListAdapter
        }
    }

    private fun setAdapter(blockUserList: ArrayList<BlockUserListResponse.Data>) {
        if (!blockUserList.isNullOrEmpty()) {
            blockedUserListAdapter.submitList(blockUserList)
            binding.rvBlockUser.visibility = View.VISIBLE
            binding.layNoRecord.visibility = View.GONE
        } else {
            binding.rvBlockUser.visibility = View.GONE
            binding.layNoRecord.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgBack ->{
               findNavController().popBackStack()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getBlockUserList()
    }
}