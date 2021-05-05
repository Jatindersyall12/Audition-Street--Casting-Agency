package com.silo.ui.base

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.auditionstreet.castingagency.ui.base.viewmodel.SharedViewModel
import com.silo.listeners.DialogHelper
import com.silo.listeners.DialogProvider
import kotlinx.android.synthetic.main.toolbar.*

abstract class BaseFragment(@LayoutRes layoutResId: Int) : Fragment(layoutResId), DialogProvider {
    protected val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var dialogHelper: DialogHelper

    override fun onAttach(context: Context) {
        dialogHelper = provideDialogHelper()
        super.onAttach(context)
    }

    protected fun showProgress() {
        dialogHelper.showProgress()
    }

    protected fun hideProgress() {
        dialogHelper.hideProgress()
    }

    protected fun setUpToolbar(
        toolbar: Toolbar,
        title: String,
        backBtnVisibility: Boolean,
        profilePic: Boolean = true
    ) {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolBarImage.visibility = if (backBtnVisibility) View.VISIBLE else View.GONE
        if (!TextUtils.isEmpty(title)) {
            toolbarTitle.text = title
        }
        if (backBtnVisibility) {
            toolbarTitle.visibility = View.GONE
            imgBack.visibility = View.VISIBLE
        } else {
            toolbarTitle.visibility = View.VISIBLE
            imgBack.visibility = View.GONE
        }
        if (!profilePic)
            toolBarImage.visibility = View.GONE
        if (!TextUtils.isEmpty(title)) {
            toolbarTitle.text = title
        }
        imgBack.setOnClickListener {
            Log.e("sd","dggf")
            findNavController().popBackStack()
            // findNavController().popBackStack()
        }
    }
}