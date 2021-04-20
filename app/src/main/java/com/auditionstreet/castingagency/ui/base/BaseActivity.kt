package com.silo.ui.base

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.silo.listeners.DialogHelper
import com.silo.listeners.DialogProvider
import com.silo.ui.base.viewmodel.SharedViewModel
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

    fun setUpToolbar(toolbar: Toolbar, title: String, backBtnVisibility: Boolean = true) {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        backBtn.visibility = if (backBtnVisibility) View.VISIBLE else View.GONE
        if (!TextUtils.isEmpty(title)) {
            toolbarTitle.text = title
        }
        backBtn.setOnClickListener {
            finish()
        }
    }
}