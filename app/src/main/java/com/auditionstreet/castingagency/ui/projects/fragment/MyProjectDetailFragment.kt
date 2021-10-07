package com.auditionstreet.castingagency.ui.projects.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.auditionstreet.castingagency.BuildConfig
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentMyProjectDetailBinding
import com.auditionstreet.castingagency.model.response.GetBodyTypeLanguageResponse
import com.auditionstreet.castingagency.model.response.MyProjectDetailResponse
import com.auditionstreet.castingagency.model.response.ProjectResponse
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.home.activity.OtherUserProfileActivity
import com.auditionstreet.castingagency.ui.home.adapter.ShortListAdapter
import com.auditionstreet.castingagency.ui.projects.adapter.ApplicantListAdapter
import com.auditionstreet.castingagency.ui.projects.viewmodel.AddProjectViewModel
import com.auditionstreet.castingagency.ui.projects.viewmodel.MyProjectDetailViewModel
import com.auditionstreet.castingagency.utils.AppConstants
import com.auditionstreet.castingagency.utils.convertToJsonString
import com.auditionstreet.castingagency.utils.showToast
import com.google.gson.Gson
import com.leo.wikireviews.utils.livedata.EventObserver
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
    private val navArgs by navArgs<MyProjectDetailFragmentArgs>()

    private val viewModel: MyProjectDetailViewModel by viewModels()
    private val viewModelAddProject: AddProjectViewModel by viewModels()
    private lateinit var applicantListAdapter: ApplicantListAdapter
    private var projectDetailResponse: MyProjectDetailResponse ?= null
    private var projectDetail = ""

    @Inject
    lateinit var preferences: Preferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setListeners()
        setObservers()
        getMyProjectDetail(navArgs.projectId)
    }

    private fun getMyProjectDetail(projectId: String) {

        viewModel.getMyProjectDetail(
            BuildConfig.BASE_URL + ApiConstant.GET_MY_PROJECTS_DETAILS + "/" + projectId
        )


    }

    private fun setListeners() {
         binding.imgEdit.setOnClickListener(this)
        binding.imgDelete.setOnClickListener(this)
    }


    private fun setObservers() {
        viewModel.users.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
        viewModel.deleteProject.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
        viewModelAddProject.bodyTypeLanguage.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
    }

    private fun init() {
        binding.rvApplicant.apply {
            layoutManager = LinearLayoutManager(activity)
            applicantListAdapter = ApplicantListAdapter(requireActivity())
            { position: Int ->

            }
            adapter = applicantListAdapter
        }
    }

    private fun setAdapter(projectList: ArrayList<MyProjectDetailResponse.Data.ProjectRequests>) {
        if (!projectList.isNullOrEmpty()) {
            applicantListAdapter.submitList(projectList)
            binding.rvApplicant.visibility = View.VISIBLE
           // binding.tvNoDataFound.visibility = View.GONE
        } else {
            binding.rvApplicant.visibility = View.GONE
         //   binding.tvNoDataFound.visibility = View.VISIBLE
        }
    }

    private fun handleApiCallback(apiResponse: Resource<Any>) {
        when (apiResponse.status) {
            Status.SUCCESS -> {
                hideProgress()
                when (apiResponse.apiConstant) {
                    ApiConstant.GET_MY_PROJECTS_DETAILS -> {
                        setDetail(apiResponse.data as MyProjectDetailResponse)
                        projectDetailResponse = apiResponse.data as MyProjectDetailResponse
                        viewModelAddProject.getLanguageBodyType(BuildConfig.BASE_URL + ApiConstant.GET_LANGUAGE_BODY_TYPE)
                    }
                    ApiConstant.DELETE_PROJECT -> {
                       findNavController().popBackStack()
                    }
                    ApiConstant.GET_LANGUAGE_BODY_TYPE -> {
                        val getBodyTypeLanguageResponse = apiResponse.data as GetBodyTypeLanguageResponse
                       val languageList = getBodyTypeLanguageResponse.data.languages
                        var languageString = ""
                        if (projectDetailResponse != null){
                            for (i in 0 until projectDetailResponse!!.data[0].projectDetails.lang.size){
                                for (y in 0 until languageList!!.size){
                                    if (projectDetailResponse!!.data[0].projectDetails.lang[i].id.toInt() ==
                                        languageList!![y].id){
                                        languageList!![y].isChecked = true
                                        languageString += languageList!![y].name + " ,"
                                        if (languageString.length >= 1)
                                            binding.tvLanguageDetail.text = languageString.substring(0, languageString.length - 1)
                                        else
                                            binding.tvLanguageDetail.text = resources.getString(R.string.str_empty)
                                    }
                                }
                            }
                           /* for (i in 0 until projectDetailResponse!!.data[0].projectDetails.bodyType.size){
                                for (y in 0 until bodyTypeList!!.size){
                                    if (projectDetailResponse!!.data[0].projectDetails.bodyType[i].toInt() ==
                                        bodyTypeList!![y].id){
                                        bodyTypeList!![y].isChecked = true
                                        bodyTypeString += bodyTypeList!![y].name + " ,"
                                        if (bodyTypeString.length >= 1)
                                            binding.etxBodyType.text = bodyTypeString.substring(0, bodyTypeString.length - 1)
                                        else
                                            binding.etxBodyType.text = ""
                                    }
                                }
                            }*/
                           /* for (i in 0 until projectDetailResponse!!.data[0].projectDetails.skinTone.size){
                                for (y in 0 until skinToneList!!.size){
                                    if (projectDetailResponse!!.data[0].projectDetails.skinTone[i].toInt() ==
                                        skinToneList!![y].id){
                                        skinToneList!![y].isChecked = true
                                        skinToneString += skinToneList!![y].name + " ,"
                                        if (skinToneString.length >= 1)
                                            binding.etxSkinType.text = skinToneString.substring(0, skinToneString.length - 1)
                                        else
                                            binding.etxSkinType.text = ""
                                    }
                                }
                            }*/
                        }
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

    private fun setDetail(myProjectResponse: MyProjectDetailResponse) {
        val gson = Gson()
        projectDetail = gson.convertToJsonString(myProjectResponse)
        setAdapter(myProjectResponse.data[0].projectRequests)
        binding.tvTitle.text = myProjectResponse.data[0].projectDetails.title
        binding.tvAgeDetail.text = myProjectResponse.data[0].projectDetails.age
        if (myProjectResponse.data[0].projectDetails.heightFt.isEmpty())
            binding.tvHeightDetail.text = resources.getString(R.string.str_empty)
        else
            binding.tvHeightDetail.text =
                myProjectResponse.data[0].projectDetails.heightFt + " ft " + myProjectResponse.data[0].projectDetails.heightIn
       /* if (myProjectResponse.data[0].projectDetails.lang.isEmpty())
            binding.tvLanguageDetail.text = resources.getString(R.string.str_empty)
        else
            binding.tvLanguageDetail.text = myProjectResponse.data[0].projectDetails.lang*/
        if (myProjectResponse.data[0].projectDetails.fromDate.isEmpty())
            binding.tvDatesDetail.text = resources.getString(R.string.str_empty)
        else
            binding.tvDatesDetail.text =
                myProjectResponse.data[0].projectDetails.fromDate + resources.getString(R.string.str_to) + myProjectResponse.data[0].projectDetails.toDate
        if (myProjectResponse.data[0].projectDetails.location.isEmpty())
            binding.tvLocationDetail.text = resources.getString(R.string.str_empty)
        else
            binding.tvLocationDetail.text = myProjectResponse.data[0].projectDetails.location
        binding.tvDescDetail.text = myProjectResponse.data[0].projectDetails.description
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.imgEdit -> {
                sharedViewModel.setDirection(
                    MyProjectDetailFragmentDirections.navigateToUpdateProject(
                        projectDetail
                    )
                )
        }
            R.id.imgDelete->{
                viewModel.deleteProject(
                    BuildConfig.BASE_URL + ApiConstant.DELETE_PROJECT + "/" + navArgs.projectId
                )
            }
    }
      }
}