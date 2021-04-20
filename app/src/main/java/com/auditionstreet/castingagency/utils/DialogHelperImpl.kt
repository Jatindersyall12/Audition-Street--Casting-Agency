package com.silo.utils

import android.app.Dialog
import android.util.Log
import android.view.WindowManager
import com.auditionstreet.castingagency.R
import com.silo.listeners.DialogHelper
import com.silo.ui.base.BaseActivity

class DialogHelperImpl(context: BaseActivity) : DialogHelper {
    private val dialog: Dialog = Dialog(context, R.style.CustomDialogTheme)

    override fun showProgress() {
        try {
            val customDialog = dialog
            Log.e("MYTAG", customDialog.toString())
            customDialog.setCancelable(false)
            customDialog.setContentView(R.layout.dialog_progress_bar)
            val lp = WindowManager.LayoutParams()
            val window = customDialog.window
            lp.copyFrom(window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            window.attributes = lp
            customDialog.show()
        } catch (e: Exception) {
            Log.e("MYTAG", e.printStackTrace().toString())
        }
    }

    override fun hideProgress() {
        val customDialog = dialog
        try {
            if (customDialog.isShowing) {
                customDialog.dismiss()
            }
        } catch (e: Exception) {
        }
    }
}