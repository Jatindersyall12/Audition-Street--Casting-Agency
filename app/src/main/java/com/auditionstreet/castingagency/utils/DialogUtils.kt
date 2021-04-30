package com.auditionstreet.castingagency.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.auditionstreet.castingagency.R

fun closeAppDialog(activity: Activity) {
    val dialogBuilder = AlertDialog.Builder(activity!!)
    dialogBuilder.setMessage("Do you want to exit?")
        // if the dialog is cancelable
        .setCancelable(false)
        .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
            dialog.dismiss()
            activity.finish()
        })
        .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
            dialog.dismiss()
        })

    val alert = dialogBuilder.create()
    alert.show()
}

fun showAdminPopUp(
    mContext: Context,
    mCallback: (year: String) -> Unit
): Dialog {

    val dialogView = Dialog(mContext)

    dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
    val binding =
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(mContext),
            R.layout.popup_admin,
            null,
            false
        )
    dialogView.setContentView(binding.root)
    dialogView.setCancelable(true)
    dialogView.show()
    return dialogView
}

