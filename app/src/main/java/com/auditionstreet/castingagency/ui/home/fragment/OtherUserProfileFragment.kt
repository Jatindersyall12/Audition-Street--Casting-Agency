package com.auditionstreet.castingagency.ui.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.auditionstreet.castingagency.BuildConfig
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentOtherUserBinding
import com.auditionstreet.castingagency.model.response.OtherProfileResponse
import com.auditionstreet.castingagency.model.response.ProjectResponse
import com.auditionstreet.castingagency.ui.home.viewmodel.OtherProfileViewModel
import com.auditionstreet.castingagency.ui.home.viewmodel.ProjectViewModel
import com.auditionstreet.castingagency.ui.projects.adapter.OtherUserImageAdapter
import com.auditionstreet.castingagency.ui.projects.adapter.OtherUserVideoAdapter
import com.auditionstreet.castingagency.ui.projects.adapter.WorkListAdapter
import com.auditionstreet.castingagency.utils.AppConstants
import com.auditionstreet.castingagency.utils.showToast
import com.bumptech.glide.Glide
import com.leo.wikireviews.utils.livedata.EventObserver
import com.silo.model.request.WorkGalleryRequest
import com.silo.utils.AppBaseFragment
import com.silo.utils.network.Resource
import com.silo.utils.network.Status
import com.silo.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherUserProfileFragment : AppBaseFragment(R.layout.fragment_other_user),
    View.OnClickListener {
    private val binding by viewBinding(FragmentOtherUserBinding::bind)
    private lateinit var otherUserImageAdapter: OtherUserImageAdapter
    private lateinit var otherUserVideoAdapter: OtherUserVideoAdapter
    private lateinit var profileAdapter: WorkListAdapter
    var listGallery = ArrayList<WorkGalleryRequest>()
    var artistId = ""

    private val viewModel: OtherProfileViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
        init()
        artistId = AppConstants.ARTISTID
        getUserProfile()
    }

    private fun getUserProfile() {
        viewModel.getProfile(
            BuildConfig.BASE_URL + ApiConstant.GET_OTHER_PROFILE + "/" +artistId
            )
    }

    private fun setListeners() {
        /*binding.tvViewAllImages.setOnClickListener(this)
        binding.tvViewAllVideos.setOnClickListener(this)*/

    }


    private fun setObservers() {
        viewModel.getProfile.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
    }

    private fun handleApiCallback(apiResponse: Resource<Any>) {
        when (apiResponse.status) {
            Status.SUCCESS -> {
                hideProgress()
                when (apiResponse.apiConstant) {
                    ApiConstant.GET_OTHER_PROFILE -> {
                        setWorkAdapter(apiResponse.data as OtherProfileResponse)
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
        binding.rvWork.apply {
            layoutManager = LinearLayoutManager(activity)
            profileAdapter = WorkListAdapter(requireActivity())
            { position: Int ->
              //  isImageDelete = listGallery[position].isImage
              /*  deleteMediaPos=position
                if (listGallery[position].isLocal) {
                    listGallery.removeAt(position)
                } else {
                    viewModel.deleteMedia(
                        BuildConfig.BASE_URL + ApiConstant.DELETE_MEDIA + "/" + preferences.getString(
                            AppConstants.USER_ID
                        )
                    )
                }
                profileAdapter.notifyDataSetChanged()*/
                if (listGallery.size > 0) {
                    showGalleryView(true)
                } else {
                    showGalleryView(false)
                }
            }

            adapter = profileAdapter
            val gridLayoutManager = GridLayoutManager(requireActivity(), 3)
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL) // set Horizontal Orientation
            binding.rvWork.setLayoutManager(gridLayoutManager)
            profileAdapter.submitList(listGallery)
        }
    }

    private fun setWorkAdapter(profileResponse: OtherProfileResponse) {

        if (profileResponse.data!![0]!!.artistDetails!!.image!!.isNotEmpty()) {
            Glide.with(this).load(profileResponse.data[0]!!.artistDetails!!.image)
                .into(binding.ivProfileImage)
        }
        binding.tvTitle.setText(profileResponse.data[0]!!.artistDetails!!.name)
        if (profileResponse.data[0]!!.artistDetails!!.gender.equals("Male")){
            binding.tvActress.text = "Actor"
        }else{
            binding.tvActress.text = "Actress"
        }
        binding.tvAge.text = "Age: "+profileResponse.data[0]!!.artistDetails!!.age
        binding.tvHeight.text = "Height: "+profileResponse.data[0]!!.artistDetails!!.heightFt+"."+
                profileResponse.data[0]!!.artistDetails!!.heightIn+" ft"
        binding.tvLanguage.text = "Language: "+profileResponse.data[0]!!.artistDetails!!.language
        binding.headingExperiance.text = profileResponse.data[0]!!.artistDetails!!.year
        binding.headingAppliedProject.text = profileResponse.data[0]!!.totalApplication.toString()
        binding.headingSelectedProject.text = profileResponse.data[0]!!.acceptedApplication.toString()
       /* if (profileResponse.data[0]!!.artistDetails!!.video!!.isNotEmpty()) {
            Glide.with(this).load(profileResponse.data[0]!!.artistDetails!!.video)
                .into(binding.imgIntroVideo)
            binding.imgPlay.visibility = View.VISIBLE
            introVideoPath = profileResponse.data[0]!!.artistDetails!!.video.toString()
        }*/
        listGallery.clear()
        for (i in 0 until profileResponse.data[0]!!.media!!.size) {
            val request = WorkGalleryRequest()
            request.path = profileResponse.data[0]!!.media!![i]!!.mediaUrl!!
            request.isShowDeleteImage = false
            request.isLocal = false
            request.isImage =
                profileResponse.data[0]!!.media!![i]!!.mediaType.equals(resources.getString(R.string.str_img))
            listGallery.add(request)
        }
        if (listGallery.size > 0) {
            showGalleryView(true)
        } else {
            showGalleryView(false)
        }
        profileAdapter.notifyDataSetChanged()
        binding.tvBio.text = profileResponse.data[0]!!.artistDetails!!.bio

    }

    private fun showGalleryView(b: Boolean) {
        if (b) {
            binding.rvWork.visibility = View.VISIBLE
            binding.tvNoMedia.visibility = View.INVISIBLE
        } else {
            binding.rvWork.visibility = View.INVISIBLE
            binding.tvNoMedia.visibility = View.VISIBLE
        }
    }

    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
          /* R.id.tvViewAllImages->
           {
               showToast(requireActivity(),resources.getString(R.string.str_coming_soon))
           }
            R.id.tvViewAllVideos->
            {
                showToast(requireActivity(),resources.getString(R.string.str_coming_soon))
            }*/
        }
    }
}