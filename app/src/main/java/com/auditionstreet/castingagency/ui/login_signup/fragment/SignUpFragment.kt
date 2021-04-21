package com.auditionstreet.castingagency.ui.login_signup.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.databinding.FragmentSignupBinding
import com.auditionstreet.castingagency.ui.login_signup.viewmodel.SignUpViewModel
import com.auditionstreet.castingagency.utils.CompressFile
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.leo.wikireviews.utils.livedata.EventObserver
import com.silo.utils.AppBaseFragment
import com.silo.utils.network.Resource
import com.silo.utils.network.Status
import com.silo.utils.showToast
import com.silo.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class SignUpFragment : AppBaseFragment(R.layout.fragment_signup), View.OnClickListener {
    private val binding by viewBinding(FragmentSignupBinding::bind)
    private val RC_CODE_PICKER = 2000
    private var images: MutableList<com.esafirm.imagepicker.model.Image> = mutableListOf()
    private var profileImageFile: File? = null
    private var selectedImage = ""
    private val viewModel: SignUpViewModel by viewModels()
    private var compressImage = CompressFile()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
    }

    private fun setListeners() {
        binding.imgProfileImage.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.imgProfileImage -> {
                pickImage()
            }
        }
    }

    private fun setObservers() {
        viewModel.signUp.observe(viewLifecycleOwner, EventObserver {
            handleApiCallback(it)
        })
    }

    private fun handleApiCallback(apiResponse: Resource<Any>) {
        when (apiResponse.status) {
            Status.SUCCESS -> {
                hideProgress()
                when (apiResponse.apiConstant) {

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

    private fun pickImage() {
        ImagePicker.create(this)
            .returnMode(ReturnMode.ALL)
            .folderMode(true)
            .single()
            .limit(1)
            .toolbarFolderTitle(getString(R.string.folder))
            .toolbarImageTitle(getString(R.string.gallery_select_title_msg))
            .start(RC_CODE_PICKER)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_CODE_PICKER && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            images = ImagePicker.getImages(data)
            profileImageFile = File(images.get(0).path)
            selectedImage = images.get(0).name
            profileImageFile = compressImage.getCompressedImageFile(profileImageFile!!, activity as Context)
            Glide.with(this).load(profileImageFile)
                .into(binding.imgProfileImage)
        }
    }
}