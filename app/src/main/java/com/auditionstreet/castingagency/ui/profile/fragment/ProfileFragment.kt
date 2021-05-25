package com.auditionstreet.castingagency.ui.profile.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.auditionstreet.castingagency.BuildConfig
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.databinding.FragmentProfileBinding
import com.auditionstreet.castingagency.model.response.DeleteMediaResponse
import com.auditionstreet.castingagency.model.response.ProfileResponse
import com.auditionstreet.castingagency.model.response.UploadMediaResponse
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.profile.viewmodel.ProfileViewModel
import com.auditionstreet.castingagency.ui.projects.adapter.WorkListAdapter
import com.auditionstreet.castingagency.utils.*
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.leo.wikireviews.utils.livedata.EventObserver
import com.silo.model.request.WorkGalleryRequest
import com.silo.utils.AppBaseFragment
import com.silo.utils.network.Resource
import com.silo.utils.network.Status
import com.silo.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.set


@AndroidEntryPoint
class ProfileFragment : AppBaseFragment(R.layout.fragment_profile), View.OnClickListener {
    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var profileAdapter: WorkListAdapter
    private val picker = 2000
    private val picker_gallery = 4000
    private var totalGalleryImages = 0
    private var totalGalleryVideos = 0
    var listGallery = ArrayList<WorkGalleryRequest>()
    private var images: MutableList<com.esafirm.imagepicker.model.Image> = mutableListOf()
    private var profileImageFile: File? = null
    private var selectedImage = ""
    private var compressImage = CompressFile()
    private lateinit var uploadMediaResponse: UploadMediaResponse
    private lateinit var mediaDelete: DeleteMediaResponse
    private var isImageDelete: Boolean = false
    private var deleteMediaPos: Int = 0
    val uploadMedia = ArrayList<WorkGalleryRequest>()

    @Inject
    lateinit var preferences: Preferences
    private lateinit var profileResponse: ProfileResponse


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
        getUserProfile()
        init()
    }

    private fun getUserProfile() {
        viewModel.getProfile(
            BuildConfig.BASE_URL + ApiConstant.GET_PROFILE + "/" + preferences.getString(
                AppConstants.USER_ID
            )
        )
    }

    private fun setListeners() {
        binding.tvAddMedia.setOnClickListener(this)
        binding.imgEdit.setOnClickListener(this)
        binding.tvDone.setOnClickListener(this)
        binding.imgProfile.setOnClickListener(this)
    }


    private fun setObservers() {
        viewModel.getProfile.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
        viewModel.uploadMedia.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
        viewModel.deleteMedia.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })

    }

    private fun handleApiCallback(apiResponse: Resource<Any>) {
        when (apiResponse.status) {
            Status.SUCCESS -> {
                hideProgress()
                when (apiResponse.apiConstant) {
                    ApiConstant.GET_PROFILE -> {
                        profileResponse = apiResponse.data as ProfileResponse
                        setWorkAdapter(profileResponse)
                    }
                    ApiConstant.UPLOAD_MEDIA -> {
                        uploadMediaResponse = apiResponse.data as UploadMediaResponse
                        showToast(requireActivity(), uploadMediaResponse.msg.toString())
                        showImgEdit()
                        enableOrDisable(false)
                        getUserProfile()
                    }
                    ApiConstant.DELETE_MEDIA -> {
                        mediaDelete = apiResponse.data as DeleteMediaResponse
                        showToast(requireActivity(), mediaDelete.msg.toString())
                        if (isImageDelete)
                            totalGalleryImages--
                        else
                            totalGalleryVideos--
                        listGallery.removeAt(deleteMediaPos)
                        profileAdapter.notifyDataSetChanged()
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

    private fun setWorkAdapter(profileResponse: ProfileResponse) {
        if (profileResponse.data!![0]!!.castingDetails!!.logo!!.isNotEmpty()) {
            Glide.with(this).load(profileResponse.data[0]!!.castingDetails!!.logo)
                .into(binding.imgProfile)
        }
        binding.etxName.setText(profileResponse.data[0]!!.castingDetails!!.name)
        binding.etxSubName.setText(profileResponse.data[0]!!.castingDetails!!.companyName)
        binding.etxYearinIndustry.setText(profileResponse.data[0]!!.castingDetails!!.year)
        listGallery.clear()
        for (i in 0 until profileResponse.data[0]!!.media!!.size) {
            val request = WorkGalleryRequest()
            request.path = profileResponse.data[0]!!.media!![i]!!.mediaUrl!!
            request.isShowDeleteImage = false
            request.isLocal = false
            request.isImage =
                profileResponse.data[0]!!.media!![i]!!.mediaType.equals(resources.getString(R.string.str_image_label))
            listGallery.add(request)
        }
        if (listGallery.size > 0) {
            showGalleryView(true)
        } else {
            showGalleryView(false)
        }
        profileAdapter.notifyDataSetChanged()

        binding.etxBio.setText(profileResponse.data[0]!!.castingDetails!!.bio)


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvAddMedia -> {
                mPermissionResult.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }
            R.id.imgEdit -> {
                showDoneButton()
                enableOrDisable(true)
            }
            R.id.tvDone -> {

                uploadMedia.clear()
                for (i in 0 until listGallery.size) {
                    if (listGallery[i].isLocal)
                        uploadMedia.add(listGallery[i])
                }
                viewModel.uploadMedia(
                    uploadMedia,
                    requestProfileUpdate(),
                    selectedImage,
                    profileImageFile
                )
            }
            R.id.imgProfile -> {
                pickImage(picker, true, 1)
            }
        }
    }

    private fun showMedia() {
        showMediaDialog(requireActivity())
        {
            if (it == 0)
                if (totalGalleryImages < 4)
                    pickImage(picker_gallery, false, 4 - totalGalleryImages)
                else
                    showVideoOrImageValidation(
                        requireActivity(),
                        resources.getString(R.string.str_image_error)
                    )
            else if (it == 1) {
                if (totalGalleryVideos < 1) {
                    val intent = Intent()
                    intent.type = "video/*"
                    intent.action = Intent.ACTION_PICK
                    Intent.createChooser(intent, "Select Video")
                    startForResult.launch(intent)
                } else
                    showVideoOrImageValidation(
                        requireActivity(),
                        resources.getString(R.string.str_video_error)
                    )
            }
        }
    }

    private fun enableOrDisable(b: Boolean) {
        binding.etxName.isEnabled = b
        binding.etxSubName.isEnabled = b
        binding.etxYear.isEnabled = b
        binding.etxBio.isEnabled = b
        binding.imgProfile.isClickable = b

        for (i in 0 until listGallery.size) {
            listGallery.get(i).isShowDeleteImage = true
            profileAdapter.notifyDataSetChanged()
        }
    }

    private fun showDoneButton() {
        binding.imgEdit.visibility = View.GONE
        binding.tvDone.visibility = View.VISIBLE
        binding.tvAddMedia.visibility = View.VISIBLE
    }

    private fun showImgEdit() {
        binding.imgEdit.visibility = View.VISIBLE
        binding.tvDone.visibility = View.GONE
        binding.tvAddMedia.visibility = View.GONE
    }

    private fun init() {
        binding.imgProfile.isClickable = false
        binding.rvWork.apply {
            layoutManager = LinearLayoutManager(activity)
            profileAdapter = WorkListAdapter(requireActivity())
            { position: Int ->
                isImageDelete = listGallery[position].isImage
                deleteMediaPos=position
                if (listGallery[position].isLocal) {
                    listGallery.removeAt(position)
                } else {
                    viewModel.deleteMedia(
                        BuildConfig.BASE_URL + ApiConstant.DELETE_MEDIA + "/" + preferences.getString(
                            AppConstants.USER_ID
                        )
                    )
                }
                profileAdapter.notifyDataSetChanged()
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

    private fun pickImage(picker: Int, showCamera: Boolean, maxCount: Int) {
        ImagePicker.create(this)
            .showCamera(showCamera)
            .folderMode(false)
            .limit(maxCount)
            .toolbarFolderTitle(getString(R.string.folder))
            .toolbarImageTitle(getString(R.string.gallery_select_title_msg))
            .start(picker)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == picker && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            images = ImagePicker.getImages(data)
            profileImageFile = File(images[0].path)
            selectedImage = images[0].name
            profileImageFile =
                compressImage.getCompressedImageFile(profileImageFile!!, activity as Context)
            Glide.with(this).load(profileImageFile)
                .into(binding.imgProfile)
        } else if (requestCode == picker_gallery && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            images = ImagePicker.getImages(data)
            for (i in 0 until images.size) {
                totalGalleryImages++
                val request = WorkGalleryRequest()
                request.path = images[i].path
                request.isImage = true
                request.isShowDeleteImage = true
                request.isLocal = true
                listGallery.add(0, request)
                profileAdapter.notifyDataSetChanged()
                showGalleryView(true)
            }
        }
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val request = WorkGalleryRequest()
                processVideo(result.data!!.data!!, requireActivity())
                { path: String ->
                    request.path = path
                    request.isImage = false
                    request.isShowDeleteImage = true
                    listGallery.add(0, request)
                    profileAdapter.notifyDataSetChanged()
                    request.isLocal = true
                    totalGalleryVideos++
                    showGalleryView(true)
                }
            }
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

    val mPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var permission: Int = 0
            permissions.entries.forEach {
                Log.e("DEBUG", "${it.key} = ${it.value}")
                if (it.value)
                    permission++
            }
            if (permission == 2)
                showMedia()
        }

    private fun requestProfileUpdate(
    ): HashMap<String, RequestBody> {
        val map = HashMap<String, RequestBody>()
        map[resources.getString(R.string.str_company_name_label)] =
            toRequestBody(etxName.text.toString())
        map[resources.getString(R.string.str_agency_name_label)] =
            toRequestBody(etxSubName.text.toString())
        map[resources.getString(R.string.str_year_label)] =
            toRequestBody(etxYear.text.toString())
        map[resources.getString(R.string.str_bio_label)] =
            toRequestBody(etxBio.text.toString())
        map[resources.getString(R.string.str_casting_id)] =
            toRequestBody(preferences.getString(AppConstants.USER_ID))

        return map
    }

    private fun toRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }
}