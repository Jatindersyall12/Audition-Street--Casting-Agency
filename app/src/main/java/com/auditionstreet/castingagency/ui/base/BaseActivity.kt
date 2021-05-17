package com.silo.ui.base

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.auditionstreet.castingagency.ui.base.viewmodel.SharedViewModel
import com.silo.listeners.DialogHelper
import com.silo.listeners.DialogProvider
import com.silo.utils.DialogHelperImpl
import kotlinx.android.synthetic.main.toolbar.*

abstract class BaseActivity : AppCompatActivity(), DialogProvider {
    protected val sharedViewModel: SharedViewModel by viewModels()

    private val dialogHelperImps by lazy {
        DialogHelperImpl(this)
    }

    override fun provideDialogHelper(): DialogHelper {
        return dialogHelperImps
    }

    inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
        crossinline bindingInflater: (LayoutInflater) -> T
    ) =
        lazy(LazyThreadSafetyMode.NONE) {
            bindingInflater.invoke(layoutInflater)
        }

    fun setUpToolbar(
        toolbar: Toolbar,
        title: String,
        backBtnVisibility: Boolean = true,
        profilePic: Boolean = true,
        ) {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
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
            finish()
        }
    }
}