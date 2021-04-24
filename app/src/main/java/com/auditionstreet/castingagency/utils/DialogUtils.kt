package com.auditionstreet.castingagency.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface

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
