package com.auditionstreet.castingagency.ui.projects.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.auditionstreet.castingagency.BuildConfig
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentAddProjectBinding
import com.auditionstreet.castingagency.model.response.AddGroupResponse
import com.auditionstreet.castingagency.model.response.AddProjectResponse
import com.auditionstreet.castingagency.model.response.AllAdminResponse
import com.auditionstreet.castingagency.model.response.AllUsersResponse
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.projects.viewmodel.AddProjectViewModel
import com.auditionstreet.castingagency.utils.*
import com.leo.wikireviews.utils.livedata.EventObserver
import com.silo.model.request.AddGroupRequest
import com.silo.model.request.AddProjectRequest
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

    @Inject
    lateinit var preferences: Preferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        binding.rangeSeekbar.setMinValue(18f).setMaxValue(50f).setMinStartValue(25f)
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
    }

    private fun setListeners() {
        this.binding.btnSubmit.setOnClickListener(this)
        this.binding.etxSubDomain.setOnClickListener(this)
        this.binding.tvStartDate.setOnClickListener(this)
        this.binding.tvEndDate.setOnClickListener(this)
        this.binding.tvAddOrEditAdmin.setOnClickListener(this)

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
    }

    private fun handleApiCallback(apiResponse: Resource<Any>) {
        when (apiResponse.status) {
            Status.SUCCESS -> {
                hideProgress()
                when (apiResponse.apiConstant) {
                    ApiConstant.GET_ALL_ADMINS -> {
                        allAdminResponse = apiResponse.data as AllAdminResponse
                    }
                    ApiConstant.GET_ALL_USER -> {
                        allUserResponse = apiResponse.data as AllUsersResponse
                    }
                    ApiConstant.ADD_PROJECT -> {
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

    private fun addProjectRequest(binding: FragmentAddProjectBinding) {
        val request = AddProjectRequest()
        request.castingId = preferences.getString(AppConstants.USER_ID)
        request.title = binding.etxTitle.text.toString()
        request.description = binding.etxDescription.text.toString()
        if (binding.chkMale.isChecked)
            request.gender = resources.getString(R.string.str_male)
        else
            request.gender = resources.getString(R.string.str_female)
        request.age = "$minAge-$maxAge"

        if (etxHeightFt.text.toString().isNotEmpty() && etxHeightIn.text.toString().isNotEmpty())
            request.height = etxHeightFt.text.toString() + "'" + etxHeightIn.text.toString()
        else if(etxHeightFt.text.toString().isNotEmpty() && etxHeightIn.text.toString().isEmpty())
            request.height = etxHeightFt.text.toString() + "'"
        else
        request.height = ""
        request.bodyType = etxBodyType.text.toString()
        request.exp = etxExperiance.text.toString()
        request.lang = etxLanguages.text.toString()
        request.fromDate = tvStartDate.text.toString()
        request.toDate = tvEndDate.text.toString()
        request.location = etxLocation.text.toString()
        request.admins = adminList
        viewModel.addProject(request)

        Log.e("id", preferences.getString(AppConstants.USER_ID))
        Log.e("title", binding.etxTitle.text.toString())
        Log.e("desc", binding.etxDescription.text.toString())
        if (binding.chkMale.isChecked)
            Log.e("gender", "male")
        else
            Log.e("gender", "female")
        Log.e("agee", minAge.toString() + "-" + maxAge.toString())
        Log.e(
            "heightt",
            etxHeightFt.text.toString() + "'" + "\"" + etxHeightIn.text.toString() + "\""
        )
        Log.e("body type", etxBodyType.text.toString())
        Log.e("exp", etxExperiance.text.toString())
        Log.e("lang", etxLanguages.text.toString())
        Log.e("from date", tvStartDate.text.toString())
        Log.e("end date", tvEndDate.text.toString())
        Log.e("location", etxLocation.text.toString())

        //val request=AddProjectRequest(null)
        //return request

    }
}
