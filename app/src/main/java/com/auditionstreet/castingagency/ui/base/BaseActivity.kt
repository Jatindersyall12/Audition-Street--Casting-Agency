package com.silo.ui.base

import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.ui.base.viewmodel.SharedViewModel
import com.auditionstreet.castingagency.utils.chat.ChatHelper
import com.auditionstreet.castingagency.utils.chat.SharedPrefsHelper
import com.auditionstreet.castingagency.utils.chat.showSnackbar
import com.quickblox.chat.QBChatService
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.users.model.QBUser
import com.silo.listeners.DialogHelper
import com.silo.listeners.DialogProvider
import com.silo.utils.DialogHelperImpl
import kotlinx.android.synthetic.main.toolbar.*

abstract class BaseActivity : AppCompatActivity(), DialogProvider {
    protected val sharedViewModel: SharedViewModel by viewModels()
    private var progressDialog: ProgressDialog? = null
    private val TAG = BaseActivity::class.java.simpleName

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

    protected fun showErrorSnackbar(@StringRes resId: Int, e: Exception?, clickListener: View.OnClickListener?) {
        val rootView = window.decorView.findViewById<View>(android.R.id.content)
        rootView?.let {
            showSnackbar(it, resId, e, R.string.dlg_retry, clickListener)
        }
    }

    protected fun showProgressDialog(@StringRes messageId: Int) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
            progressDialog!!.isIndeterminate = true
            progressDialog!!.setCancelable(false)
            progressDialog!!.setCanceledOnTouchOutside(false)

            // Disable the back button
            val keyListener = DialogInterface.OnKeyListener { dialog,
                                                              keyCode,
                                                              event ->
                keyCode == KeyEvent.KEYCODE_BACK
            }
            progressDialog!!.setOnKeyListener(keyListener)
        }
        progressDialog!!.setMessage(getString(messageId))
        try {
            progressDialog!!.show()
        } catch (e: Exception) {
            e.message?.let { Log.d(TAG, it) }
        }
    }

    protected fun hideProgressDialog() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    protected fun isProgresDialogShowing(): Boolean {
        if (progressDialog != null && progressDialog?.isShowing != null) {
            return progressDialog!!.isShowing
        } else {
            return false
        }
    }

    override fun onResume() {
        super.onResume()
        val currentUser = ChatHelper.getCurrentUser()
        hideNotifications()
        if (currentUser != null && !QBChatService.getInstance().isLoggedIn) {
            Log.d(TAG, "Resuming with Relogin")
            ChatHelper.login(SharedPrefsHelper.getQbUser()!!, object : QBEntityCallback<QBUser> {
                override fun onSuccess(qbUser: QBUser?, b: Bundle?) {
                    Log.d(TAG, "Relogin Successful")
                    reloginToChat()
                }

                override fun onError(e: QBResponseException?) {
                    e?.message?.let { Log.d(TAG, it) }
                }
            })

        } else {
            Log.d(TAG, "Resuming without Relogin to Chat")
            onResumeFinished()
        }
    }

    private fun reloginToChat() {
        ChatHelper.loginToChat(SharedPrefsHelper.getQbUser()!!, object : QBEntityCallback<Void> {
            override fun onSuccess(aVoid: Void?, bundle: Bundle?) {
                Log.d(TAG, "Relogin to Chat Successful")
                onResumeFinished()
            }

            override fun onError(e: QBResponseException?) {
                Log.d(TAG, "Relogin to Chat Error: " + e?.message)
                onResumeFinished()
            }
        })
    }

    private fun hideNotifications() {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    open fun onResumeFinished() {
        // Need to Override onResumeFinished() method in nested classes if we need to handle returning from background in Activity
    }
}