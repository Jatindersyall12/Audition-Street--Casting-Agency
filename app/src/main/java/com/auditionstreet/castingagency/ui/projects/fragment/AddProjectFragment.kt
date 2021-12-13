package com.auditionstreet.castingagency.ui.projects.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.auditionstreet.castingagency.BuildConfig
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentAddProjectBinding
import com.auditionstreet.castingagency.model.BodyTypeModel
import com.auditionstreet.castingagency.model.LanguageModel
import com.auditionstreet.castingagency.model.response.*
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.projects.viewmodel.AddProjectViewModel
import com.auditionstreet.castingagency.utils.*
import com.google.gson.Gson
import com.leo.wikireviews.utils.livedata.EventObserver
import com.silo.model.request.AddGroupRequest
import com.silo.model.request.AddProjectRequest
import com.silo.model.request.UpdateProjectRequest
import com.silo.utils.AppBaseFragment
import com.silo.utils.network.Resource
import com.silo.utils.network.Status
import com.silo.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_project.*
import javax.inject.Inject


@AndroidEntryPoint
class AddProjectFragment : AppBaseFragment(R.layout.fragment_add_project), View.OnClickListener {
    private val binding by viewBinding(FragmentAddProjectBinding::bind)
    private var calDay: Int = 0
    private var calMonth: Int = 0
    private var calYear: Int = 0
    private var minAge: Long = 25
    private var maxAge: Long = 40

    var adminList = arrayListOf<String>()
    var groupList = arrayListOf<String>()

    private val viewModel: AddProjectViewModel by viewModels()
    private lateinit var allAdminResponse: AllAdminResponse
    private lateinit var allUserResponse: AllUsersResponse
    private lateinit var groupResponse: AddGroupResponse
    private lateinit var addProjectResponse: AddProjectResponse
    private lateinit var getBodyTypeLanguageResponse: GetBodyTypeLanguageResponse
    private var projectDetailResponse: MyProjectDetailResponse ?= null
    private var languageList: ArrayList<GetBodyTypeLanguageResponse.Data.Language> ?= null
    private var bodyTypeList: ArrayList<GetBodyTypeLanguageResponse.Data.BodyType> ?= null
    private var skinToneList: ArrayList<GetBodyTypeLanguageResponse.Data.SkinTone> ?= null
    private var isUpdateCase = false

    private val navArgs by navArgs<AddProjectFragmentArgs>()

    @Inject
    lateinit var preferences: Preferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * Update case
         */
        if (!navArgs.projectDetail.isNullOrEmpty()) {
            val gson = Gson()
            projectDetailResponse = gson.convertToModel(
                navArgs.projectDetail,
                MyProjectDetailResponse::class.java
            )!!
            setData(
                gson.convertToModel(
                    navArgs.projectDetail,
                    MyProjectDetailResponse::class.java
                )!!
            )
            binding.btnSubmit.text = resources.getString(R.string.update)
            isUpdateCase = true
            disableViews()
        }else{
            isUpdateCase = false
        }
        languageList = ArrayList()
        bodyTypeList = ArrayList()
        skinToneList = ArrayList()

        setListeners()
        setObservers()
        getAllAdmins()
        setSeekBarListsener()
    }

    private fun setSeekBarListsener() {
        binding.rangeSeekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            binding.tvAge.text =
                resources.getString(R.string.str_age_range) + minValue.toString() + "-" + maxValue.toString()
            minAge = minValue as Long
            maxAge = maxValue as Long
        }
        binding.rangeSeekbar.setMinValue(1f).setMaxValue(100f).setMinStartValue(25f)
            .setMaxStartValue(40f)
            .apply()
    }

    private fun getAllAdmins() {

        viewModel.getAllAdmin(
            BuildConfig.BASE_URL + ApiConstant.GET_ALL_ADMINS + "/" + preferences.getString(
                AppConstants.USER_ID
            )
        )
        viewModel.getAllUser(
            BuildConfig.BASE_URL + ApiConstant.GET_ALL_USER + "/" + preferences.getString(
                AppConstants.USER_ID
            )
        )
        viewModel.getLanguageBodyType(BuildConfig.BASE_URL + ApiConstant.GET_LANGUAGE_BODY_TYPE)
    }

    private fun setListeners() {
        this.binding.btnSubmit.setOnClickListener(this)
        this.binding.etxSubDomain.setOnClickListener(this)
        this.binding.tvStartDate.setOnClickListener(this)
        this.binding.tvEndDate.setOnClickListener(this)
        this.binding.tvAddOrEditAdmin.setOnClickListener(this)
        this.binding.etxLanguages.setOnClickListener(this)
        this.binding.etxBodyType.setOnClickListener(this)
        this.binding.etxSkinType.setOnClickListener(this)

    }


    private fun setObservers() {
        viewModel.allAdmin.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
        viewModel.allUser.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
        viewModel.addProject.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
        viewModel.addGroup.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
        viewModel.bodyTypeLanguage.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
    }

    private fun handleApiCallback(apiResponse: Resource<Any>) {
        when (apiResponse.status) {
            Status.SUCCESS -> {
                hideProgress()
                when (apiResponse.apiConstant) {
                    ApiConstant.GET_ALL_ADMINS -> {
                        allAdminResponse = apiResponse.data as AllAdminResponse
                        var user = ""
                        if (!allAdminResponse.data.isNullOrEmpty() &&
                            projectDetailResponse != null){
                            for (i in 0 until projectDetailResponse!!.data[0].admins.size){
                                for (y in 0 until allAdminResponse.data!!.size){
                                    if (projectDetailResponse!!.data[0].admins[i].id ==
                                        allAdminResponse.data!![y]!!.id){
                                        allAdminResponse.data!![y]!!.is_checked = true
                                        user += allAdminResponse.data!![i]!!.name + " ,"
                                        if (user.length >= 1)
                                            binding.etxSubDomain.text = user.substring(0, user.length - 1)
                                        else
                                            binding.etxSubDomain.text = ""
                                    }
                                }
                            }
                        }
                    }
                    ApiConstant.GET_ALL_USER -> {
                        allUserResponse = apiResponse.data as AllUsersResponse
                    }
                    ApiConstant.ADD_PROJECT -> {
                        addProjectResponse = apiResponse.data as AddProjectResponse
                        showToast(requireActivity(), addProjectResponse.msg.toString())
                        findNavController().popBackStack()
                    }
                    ApiConstant.UPDATE_PROJECT -> {
                        addProjectResponse = apiResponse.data as AddProjectResponse
                        showToast(requireActivity(), addProjectResponse.msg.toString())
                        findNavController().popBackStack()
                    }
                    ApiConstant.ADD_GROUP -> {
                        groupResponse = apiResponse.data as AddGroupResponse
                        showToast(requireActivity(), groupResponse.msg)
                        adminList = arrayListOf<String>()
                        binding.etxSubDomain.text = ""
                        viewModel.getAllAdmin(
                            BuildConfig.BASE_URL + ApiConstant.GET_ALL_ADMINS + "/" + preferences.getString(
                                AppConstants.USER_ID
                            )
                        )
                    }
                    ApiConstant.GET_LANGUAGE_BODY_TYPE -> {
                        getBodyTypeLanguageResponse = apiResponse.data as GetBodyTypeLanguageResponse
                        languageList = getBodyTypeLanguageResponse.data.languages
                        bodyTypeList = getBodyTypeLanguageResponse.data.bodyTypes
                        skinToneList = getBodyTypeLanguageResponse.data.skinTones
                        var languageString = ""
                        var bodyTypeString = ""
                        var skinToneString = ""
                        if (projectDetailResponse != null){
                            for (i in 0 until projectDetailResponse!!.data[0].projectDetails.lang.size){
                                for (y in 0 until languageList!!.size){
                                    if (projectDetailResponse!!.data[0].projectDetails.lang[i].id.toInt() ==
                                        languageList!![y].id){
                                        languageList!![y].isChecked = true
                                        languageString += languageList!![y].name + " ,"
                                        if (languageString.length >= 1)
                                            binding.etxLanguages.text = languageString.substring(0, languageString.length - 1)
                                        else
                                            binding.etxLanguages.text = ""
                                    }
                                }
                            }
                            for (i in 0 until projectDetailResponse!!.data[0].projectDetails.bodyType.size){
                                for (y in 0 until bodyTypeList!!.size){
                                    if (projectDetailResponse!!.data[0].projectDetails.bodyType[i].id.toInt() ==
                                        bodyTypeList!![y].id){
                                        bodyTypeList!![y].isChecked = true
                                        bodyTypeString += bodyTypeList!![y].name + " ,"
                                        if (bodyTypeString.length >= 1)
                                            binding.etxBodyType.text = bodyTypeString.substring(0, bodyTypeString.length - 1)
                                        else
                                            binding.etxBodyType.text = ""
                                    }
                                }
                            }
                            for (i in 0 until projectDetailResponse!!.data[0].projectDetails.skinTone.size){
                                for (y in 0 until skinToneList!!.size){
                                    if (projectDetailResponse!!.data[0].projectDetails.skinTone[i].id.toInt() ==
                                        skinToneList!![y].id){
                                        skinToneList!![y].isChecked = true
                                        skinToneString += skinToneList!![y].name + " ,"
                                        if (skinToneString.length >= 1)
                                            binding.etxSkinType.text = skinToneString.substring(0, skinToneString.length - 1)
                                        else
                                            binding.etxSkinType.text = ""
                                    }
                                }
                            }
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSubmit -> {
                if (viewModel.isValidate(binding))
                    addProjectRequest(binding)
            }
            R.id.etxSubDomain -> {
                showAdminPopUpAdmins(requireActivity(), allAdminResponse)
                {
                    adminList = arrayListOf<String>()
                    var user = ""
                    for (i in 0 until allAdminResponse.data!!.size) {
                        if (allAdminResponse.data!![i]!!.is_checked) {
                            adminList.add(allAdminResponse.data!![i]!!.id!!)
                            user += allAdminResponse.data!![i]!!.name + " ,"
                        }
                    }
                    if (user.length >= 1)
                        binding.etxSubDomain.text = user.substring(0, user.length - 1)
                    else
                        binding.etxSubDomain.text = ""
                }
            }
            R.id.etxLanguages ->{
                showLanguageSelectionDialog(requireActivity(), languageList!!)
                {
                   val languageStringList = arrayListOf<String>()
                    var user = ""
                    for (i in 0 until languageList!!.size) {
                        if (languageList!![i].isChecked) {
                            languageStringList.add(languageList!![i].name)
                            user += languageList!![i].name + " ,"
                        }
                    }
                    if (user.length >= 1)
                        binding.etxLanguages.text = user.substring(0, user.length - 1)
                    else
                        binding.etxLanguages.text = ""
                }
            }
            R.id.etxBodyType ->{
                showBodyTypeSelectionDialog(requireActivity(), bodyTypeList!!)
                {
                    val bodyTypeStringList = arrayListOf<String>()
                    var user = ""
                    for (i in 0 until bodyTypeList!!.size) {
                        if (bodyTypeList!![i].isChecked) {
                            bodyTypeStringList.add(bodyTypeList!![i].name)
                            user += bodyTypeList!![i].name + " ,"
                        }
                    }
                    if (user.length >= 1)
                        binding.etxBodyType.text = user.substring(0, user.length - 1)
                    else
                        binding.etxBodyType.text = ""
                }
            }
            R.id.etxSkinType ->{
                showSkinToneSelectionDialog(requireActivity(), skinToneList!!)
                {
                    val bodyTypeStringList = arrayListOf<String>()
                    var user = ""
                    for (i in 0 until skinToneList!!.size) {
                        if (skinToneList!![i].isChecked) {
                            bodyTypeStringList.add(skinToneList!![i].name)
                            user += skinToneList!![i].name + " ,"
                        }
                    }
                    if (user.length >= 1)
                        binding.etxSkinType.text = user.substring(0, user.length - 1)
                    else
                        binding.etxSkinType.text = ""
                }
            }
            R.id.tvAddOrEditAdmin -> {
                showAllUser(requireActivity(), allUserResponse)
                {
                    groupList = arrayListOf<String>()
                    for (i in 0 until allUserResponse.data.size) {
                        if (allUserResponse.data[i].isChecked) {
                            groupList.add(allUserResponse.data[i].id.toString())
                        }
                    }
                    //  if (groupList.size > 0) {
                    val request = AddGroupRequest()
                    request.castingId = preferences.getString(AppConstants.USER_ID)
                    request.anotherCastingId = groupList
                    viewModel.createGroup(request)
                    //  }
                }
            }
            R.id.tvStartDate -> {
                showFromDatePicker(requireActivity()) { day: Int, month: Int, year: Int ->
                    calDay = day
                    calMonth = month
                    calYear = year
                    binding.tvStartDate.text = formatDate(requireActivity(), day, month, year)
                }
            }
            R.id.tvEndDate -> {
                if (tvStartDate.text.isEmpty())
                    showToast(requireActivity(), resources.getString(R.string.str_start_Date))
                else
                    showToDatePicker(
                        requireActivity(),
                        calDay,
                        calMonth,
                        calYear
                    ) { day: Int, month: Int, year: Int ->
                        binding.tvEndDate.text = formatDate(requireActivity(), day, month, year)

                    }
            }
        }
    }

    private fun setData(myProjectResponse: MyProjectDetailResponse) {
        binding.etxTitle.setText(myProjectResponse.data[0].projectDetails.title)
        binding.tvAge.setText(myProjectResponse.data[0].projectDetails.age)
        binding.etxHeightFt.setText(myProjectResponse.data[0].projectDetails.heightFt)
        binding.etxHeightIn.setText(myProjectResponse.data[0].projectDetails.heightIn)
        binding.tvStartDate.text = myProjectResponse.data[0].projectDetails.fromDate
        binding.tvEndDate.text = myProjectResponse.data[0].projectDetails.toDate
        binding.etxLocation.setText(myProjectResponse.data[0].projectDetails.location)
        binding.etxDescription.setText(myProjectResponse.data[0].projectDetails.description)
        binding.etxExperiance.setText(myProjectResponse.data[0].projectDetails.exp)
        val separate1 = myProjectResponse.data[0].projectDetails.age.split("-")[0]
        val separate2 = myProjectResponse.data[0].projectDetails.age.split("-")[1]
        binding.rangeSeekbar.setMinValue(separate1.toFloat())
        binding.rangeSeekbar.setMaxValue(separate2.toFloat())
        if (myProjectResponse.data[0].projectDetails.gender.equals("Male")) {
            binding.chkMale.isChecked = true
            binding.chkFemale.isChecked = false
        } else {
            binding.chkFemale.isChecked = true
            binding.chkMale.isChecked = false
        }
    }

    private fun addProjectRequest(binding: FragmentAddProjectBinding) {
        val bodyTypeIdList = ArrayList<Int>()
        val languageIdList = ArrayList<Int>()
        val skinToneIdList = ArrayList<Int>()
        for (i in 0 until skinToneList!!.size){
            if (skinToneList!![i].isChecked){
                skinToneIdList.add(skinToneList!![i].id)
            }
        }
        for (i in 0 until bodyTypeList!!.size){
            if (bodyTypeList!![i].isChecked){
                bodyTypeIdList.add(bodyTypeList!![i].id)
            }
        }
        for (i in 0 until languageList!!.size){
            if (languageList!![i].isChecked){
                languageIdList.add(languageList!![i].id)
            }
        }
        if (isUpdateCase){
        val request = UpdateProjectRequest()
            request.castingId = preferences.getString(AppConstants.USER_ID)
            request.title = binding.etxTitle.text.toString()
            request.description = binding.etxDescription.text.toString()
            if (binding.chkMale.isChecked)
                request.gender = resources.getString(R.string.str_male)
            else
                request.gender = resources.getString(R.string.str_female)
            request.age = "$minAge-$maxAge"
            request.heightFt = etxHeightFt.text.toString()
            request.heightIn = etxHeightIn.text.toString()
            request.bodyType = bodyTypeIdList
            request.exp = etxExperiance.text.toString()
            request.lang = languageIdList
            request.skinTone = skinToneIdList
            request.fromDate = tvStartDate.text.toString()
            request.toDate = tvEndDate.text.toString()
            request.location = etxLocation.text.toString()
          //  request.admins = adminList
            request.projectId = projectDetailResponse!!.data[0].projectDetails.id.toString()
            viewModel.updateProject(request)
        }else {
            val request = AddProjectRequest()
            request.castingId = preferences.getString(AppConstants.USER_ID)
            request.title = binding.etxTitle.text.toString()
            request.description = binding.etxDescription.text.toString()
            if (binding.chkMale.isChecked)
                request.gender = resources.getString(R.string.str_male)
            else
                request.gender = resources.getString(R.string.str_female)
            request.age = "$minAge-$maxAge"
            request.heightFt = etxHeightFt.text.toString()
            request.heightIn = etxHeightIn.text.toString()
            request.bodyType = bodyTypeIdList
            request.exp = etxExperiance.text.toString()
            request.lang = languageIdList
            request.skinTone = skinToneIdList
            request.fromDate = tvStartDate.text.toString()
            request.toDate = tvEndDate.text.toString()
            request.location = etxLocation.text.toString()
            request.admins = adminList
            viewModel.addProject(request)
        }
    }

    private fun disableViews(){
        binding.chkMale.isEnabled = false
        binding.chkFemale.isEnabled = false
        binding.rangeSeekbar.isEnabled = false
        binding.etxHeightFt.isEnabled = false
        binding.etxHeightIn.isEnabled = false
        binding.etxLanguages.isEnabled = false
        binding.tvStartDate.isEnabled = false
        binding.tvEndDate.isEnabled = false
        binding.etxLocation.isEnabled = false
        binding.etxExperiance.isEnabled = false
        binding.etxBodyType.isEnabled = false
        binding.etxSkinType.isEnabled = false
        binding.etxSubDomain.isEnabled = false
    }
}
