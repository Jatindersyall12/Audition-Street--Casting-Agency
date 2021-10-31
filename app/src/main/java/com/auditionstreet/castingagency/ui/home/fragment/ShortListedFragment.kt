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
import com.auditionstreet.castingagency.databinding.FragmentShortListBinding
import com.auditionstreet.castingagency.model.response.MyProjectDetailResponse
import com.auditionstreet.castingagency.model.response.MyProjectResponse
import com.auditionstreet.castingagency.model.response.ProjectResponse
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.chat.DialogsActivity
import com.auditionstreet.castingagency.ui.home.activity.OtherUserProfileActivity
import com.auditionstreet.castingagency.ui.home.adapter.ProjectListAdapter
import com.auditionstreet.castingagency.ui.home.adapter.ShortListAdapter
import com.auditionstreet.castingagency.ui.home.viewmodel.ProjectViewModel
import com.auditionstreet.castingagency.utils.AppConstants
import com.auditionstreet.castingagency.utils.showSelectProjectDialog
import com.auditionstreet.castingagency.utils.showToast
import com.leo.wikireviews.utils.livedata.EventObserver
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser
import com.silo.utils.AppBaseFragment
import com.silo.utils.network.Resource
import com.silo.utils.network.Status
import com.silo.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShortListedFragment : AppBaseFragment(R.layout.fragment_short_list), View.OnClickListener {
    private val binding by viewBinding(FragmentShortListBinding::bind)
    private lateinit var shortListAdapter: ShortListAdapter
    private val viewModel: ProjectViewModel by viewModels()
    private var shortListedList: ArrayList<ProjectResponse.Data> ?= null
    private var selectedShortListedList: ArrayList<ProjectResponse.Data> ?= null
    private var projectListResponse: MyProjectResponse?= null

    @Inject
    lateinit var preferences: Preferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shortListedList = ArrayList()
        selectedShortListedList = ArrayList()
        setListeners()
        setObservers()
        init()
        getProjectList()
    }

    private fun setListeners() {
        binding.tvSelectProject.setOnClickListener(this)
    }


    private fun setObservers() {
        viewModel.users.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })

        viewModel.getMyProjects.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
    }

    private fun getProjectList(){
        viewModel.getProject(
            BuildConfig.BASE_URL + ApiConstant.GET_SHORTLISTED_LIST + "/" + preferences.getString(
                AppConstants.USER_ID
            )
        )

        viewModel.getMyProject(
            BuildConfig.BASE_URL + ApiConstant.GET_MY_PROJECTS + "/" + preferences.getString(
                AppConstants.USER_ID
            )
        )
    }

    private fun handleApiCallback(apiResponse: Resource<Any>) {
        when (apiResponse.status) {
            Status.SUCCESS -> {
                hideProgress()
                when (apiResponse.apiConstant) {
                    ApiConstant.GET_SHORTLISTED_LIST -> {
                        val response = apiResponse.data as ProjectResponse
                        setAdapter(response.data)
                        shortListedList = response.data
                    }
                    ApiConstant.GET_MY_PROJECTS -> {
                        projectListResponse = apiResponse.data as MyProjectResponse
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
        binding.rvShortList.apply {
            layoutManager = LinearLayoutManager(activity)
            shortListAdapter = ShortListAdapter(requireActivity())
            { position: Int, isViewProfileClicked: Boolean ->
                if (isViewProfileClicked) {
                    AppConstants.ARTISTID = shortListedList!![position].artistId.toString()
                    val i = Intent(requireActivity(), OtherUserProfileActivity::class.java)
                    startActivity(i)
                }else{
                    loadChatUsersFromQB(shortListedList!![position].email)
                }
            }
            adapter = shortListAdapter
        }
    }
    private fun setAdapter(projectList: ArrayList<ProjectResponse.Data>) {
        if (!projectList.isNullOrEmpty()) {
            shortListAdapter.submitList(projectList)
            binding.rvShortList.visibility = View.VISIBLE
            binding.tvNoDataFound.visibility = View.GONE
        } else {
            binding.rvShortList.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.VISIBLE
        }
    }

    private fun showSelectProject() {
        showSelectProjectDialog(requireActivity(), projectListResponse!!)
        { projectId: String ->
            selectedShortListedList!!.clear()
            for (i in 0 until shortListedList!!.size){
                if (shortListedList!![i].projectId == projectId){
                    selectedShortListedList!!.add(shortListedList!![i])
                }
            }
            setAdapter(selectedShortListedList!!)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvSelectProject -> {
                showSelectProject()
            }
        }
    }

    /**
     * Get Chat User List
     */

    private fun loadChatUsersFromQB(email: String) {
        loadUsersWithoutQuery(email)
    }

    private fun loadUsersWithoutQuery(email: String) {
        showProgress()
        QBUsers.getUserByLogin(email).performAsync(object : QBEntityCallback<QBUser> {
            override fun onSuccess(qbUser: QBUser, params: Bundle?) {
                hideProgress()
                Log.e("user", "yes")
                val i = Intent(requireActivity(), DialogsActivity::class.java)
                i.putExtra(EXTRA_QB_USERS, qbUser)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                activity!!.finish()
            }

            override fun onError(e: QBResponseException) {
                hideProgress()
                Log.e("user", "No")
                showToast(requireActivity(),"No User Found")           }
        })
    }
}