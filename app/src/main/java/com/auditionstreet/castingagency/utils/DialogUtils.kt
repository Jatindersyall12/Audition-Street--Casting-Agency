package com.auditionstreet.castingagency.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.customviews.CustomButton
import com.auditionstreet.castingagency.customviews.CustomTextView
import com.auditionstreet.castingagency.model.response.AllAdminResponse
import com.auditionstreet.castingagency.model.response.AllUsersResponse
import com.auditionstreet.castingagency.model.response.MyProjectResponse
import com.auditionstreet.castingagency.ui.home.adapter.SelectProjectListAdapter
import com.auditionstreet.castingagency.ui.projects.adapter.AllAdminListAdapter
import com.auditionstreet.castingagency.ui.projects.adapter.AllUserListAdapter
import com.bumptech.glide.Glide
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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


fun showImageOrVideoDialog(
    mContext: Context, url: String
): Dialog {
    val dialogView = Dialog(mContext)
    dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
    val binding =
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(mContext),
            R.layout.pop_up_image_video,
            null,
            false
        )
    dialogView.setContentView(binding.root)
    //dialogView.setCancelable(false)
    dialogView.show()
    val imgPopUp = dialogView.findViewById<ImageView>(R.id.imgPopUp)
    Glide.with(mContext).load(url)
        .into(imgPopUp)
    val width = (mContext.getResources().getDisplayMetrics().widthPixels * 0.90)
    val height = 750
    dialogView.getWindow()!!.setLayout(width.toInt(), height.toInt())
    return dialogView
}

fun showExitDialog(
    mContext: Activity,
): Dialog {
    val dialogView = Dialog(mContext)
    dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
    val binding =
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(mContext),
            R.layout.popup_exit,
            null,
            false
        )
    dialogView.setContentView(binding.root)
    dialogView.setCancelable(false)
    dialogView.show()
    val tvYes = dialogView.findViewById<CustomTextView>(R.id.tvYes)
    val tvNo = dialogView.findViewById<CustomTextView>(R.id.tvNo)
    tvYes.setOnClickListener {
        dialogView.dismiss()
        mContext.finish()
    }
    tvNo.setOnClickListener {
        dialogView.dismiss()
    }

    val width = (mContext.getResources().getDisplayMetrics().widthPixels * 0.90)
    val height = 450
    dialogView.getWindow()!!.setLayout(width.toInt(), height.toInt())
    return dialogView
}

fun showMediaDialog(
    mContext: Context,
    mCallback: (pos: Int) -> Unit

): Dialog {
    val dialogView = Dialog(mContext)
    dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
    val binding =
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(mContext),
            R.layout.popup_choose_media,
            null,
            false
        )
    dialogView.setContentView(binding.root)
    dialogView.setCancelable(false)
    dialogView.show()
    val tvImages = dialogView.findViewById<CustomTextView>(R.id.tvChooseImages)
    val tvVides = dialogView.findViewById<CustomTextView>(R.id.tvChooseVideos)
    val tvCancel = dialogView.findViewById<CustomTextView>(R.id.tvCancel)

    tvImages.setOnClickListener {
        mCallback.invoke(0)
        dialogView.dismiss()
    }
    tvVides.setOnClickListener {
        dialogView.dismiss()
        mCallback.invoke(1)
    }
    tvCancel.setOnClickListener {
        dialogView.dismiss()
        mCallback.invoke(2)
    }

    val width = (mContext.getResources().getDisplayMetrics().widthPixels * 0.90)
    val height = 600
    dialogView.getWindow()!!.setLayout(width.toInt(), height.toInt())
    return dialogView
}


fun showAllUser(
    mContext: Context,
    allUserResponse: AllUsersResponse,
    mCallback: (year: String) -> Unit
): Dialog {
    lateinit var rvUserAdapter: AllUserListAdapter
    val dialogView = Dialog(mContext)
    dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
    val binding =
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(mContext),
            R.layout.popup_all_user,
            null,
            false
        )
    dialogView.setContentView(binding.root)
    dialogView.setCancelable(false)
    val rvAllUser = dialogView.findViewById<RecyclerView>(R.id.rvAllUser)
    val btnDone = dialogView.findViewById<CustomButton>(R.id.btnDone)

    btnDone.setOnClickListener {
        dialogView.cancel()
        mCallback.invoke("Sd")
    }
    val tvNoRecord = dialogView.findViewById<CustomTextView>(R.id.tvNoRecord)

    if (allUserResponse.data!!.size > 0) {
        rvAllUser.visibility = View.VISIBLE
        tvNoRecord.visibility = View.GONE
    } else {
        rvAllUser.visibility = View.GONE
        tvNoRecord.visibility = View.VISIBLE
    }
    rvAllUser.apply {
        layoutManager = LinearLayoutManager(mContext)
        rvUserAdapter = AllUserListAdapter(mContext as FragmentActivity)
        { projectId: String ->

        }
        adapter = rvUserAdapter
        rvUserAdapter.submitList(allUserResponse.data)
    }

    dialogView.show()
    val width = (mContext.getResources().getDisplayMetrics().widthPixels * 0.90)
    val height = (mContext.getResources().getDisplayMetrics().heightPixels * 0.65)
    dialogView.getWindow()!!.setLayout(width.toInt(), height.toInt())
    return dialogView
}

fun showAdminPopUpAdmins(
    mContext: Context,
    allAdminResponse: AllAdminResponse,
    mCallback: (year: String) -> Unit
): Dialog {
    lateinit var rvAdminAdapter: AllAdminListAdapter
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
    dialogView.setCancelable(false)
    val rvAllUser = dialogView.findViewById<RecyclerView>(R.id.rvAdmin)
    val btnDone = dialogView.findViewById<CustomButton>(R.id.btnDone)
    val tvNoRecord = dialogView.findViewById<CustomTextView>(R.id.tvNoRecord)

    btnDone.setOnClickListener {
        dialogView.cancel()
        mCallback.invoke("Sd")
    }
    if (allAdminResponse.data!!.size > 0) {
        rvAllUser.visibility = View.VISIBLE
        tvNoRecord.visibility = View.GONE
    } else {
        rvAllUser.visibility = View.GONE
        tvNoRecord.visibility = View.VISIBLE
    }
    rvAllUser.apply {
        layoutManager = LinearLayoutManager(mContext)
        rvAdminAdapter = AllAdminListAdapter(mContext as FragmentActivity)
        { projectId: String ->

        }
        adapter = rvAdminAdapter
        allAdminResponse.data
        rvAdminAdapter.submitList(allAdminResponse.data)
    }

    dialogView.show()
    val width = (mContext.getResources().getDisplayMetrics().widthPixels * 0.90)
    val height = (mContext.getResources().getDisplayMetrics().heightPixels * 0.65)
    dialogView.getWindow()!!.setLayout(width.toInt(), height.toInt())
    return dialogView
}

fun showSelectProjectDialog(
    mContext: Context,
    projectResponse: MyProjectResponse,
    mCallback: (year: String) -> Unit
): Dialog {
    lateinit var rvSelectProject: SelectProjectListAdapter
    val dialogView = Dialog(mContext)
    dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
    val binding =
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(mContext),
            R.layout.popup_select_project,
            null,
            false
        )
    dialogView.setContentView(binding.root)
    dialogView.setCancelable(false)
    val rvAllUser = dialogView.findViewById<RecyclerView>(R.id.rvAdmin)
    val btnDone = dialogView.findViewById<CustomButton>(R.id.btnDone)
    val tvNoRecord = dialogView.findViewById<CustomTextView>(R.id.tvNoRecord)

    btnDone.setOnClickListener {
        dialogView.cancel()
        mCallback.invoke("Sd")
    }
    if (projectResponse.data!!.size > 0) {
        rvAllUser.visibility = View.VISIBLE
        tvNoRecord.visibility = View.GONE
    } else {
        rvAllUser.visibility = View.GONE
        tvNoRecord.visibility = View.VISIBLE
    }
    rvAllUser.apply {
        layoutManager = LinearLayoutManager(mContext)
        rvSelectProject = SelectProjectListAdapter(mContext as FragmentActivity)
        { projectId: String ->
            Log.e("sd", "Ss")
        }
        adapter = rvSelectProject
        rvSelectProject.submitList(projectResponse.data)
    }

    dialogView.show()
    val width = (mContext.getResources().getDisplayMetrics().widthPixels * 0.90)
    val height = (mContext.getResources().getDisplayMetrics().heightPixels * 0.65)
    dialogView.getWindow()!!.setLayout(width.toInt(), height.toInt())
    return dialogView
}


fun showFromDatePicker(
    requireActivity: FragmentActivity,
    mCallback: (day: Int, month: Int, year: Int) -> Unit
) {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(requireActivity, { view, year, monthOfYear, dayOfMonth ->
        monthOfYear
        mCallback.invoke(dayOfMonth, monthOfYear + 1, year)
    }, year, month, day)
    dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)
    dpd.show()
}

fun showToDatePicker(
    requireActivity: FragmentActivity,
    day: Int,
    month: Int,
    year: Int,
    mCallback: (day: Int, month: Int, year: Int) -> Unit
) {
    val calendar = Calendar.getInstance()

    calendar.set(Calendar.MONTH, month - 1)
    calendar.set(Calendar.DAY_OF_MONTH, day)
    calendar.set(Calendar.YEAR, year)

    val dpd = DatePickerDialog(requireActivity, { view, year, monthOfYear, dayOfMonth ->
        monthOfYear
        mCallback.invoke(dayOfMonth, monthOfYear + 1, year)
    }, year, month - 1, day)
    dpd.getDatePicker().setMinDate(calendar.timeInMillis)
    dpd.show()
}

fun formatDate(context: Context, dayOfMonth: Int, monthOfYear: Int, year: Int): String {
    val date = "$dayOfMonth/$monthOfYear/$year"
    val input = SimpleDateFormat(context.resources.getString(R.string.dd_mm_yy))
    val output = SimpleDateFormat(context.resources.getString(R.string.mm_dd_yy))
    try {
        var oneWayTripDate = input.parse(date)
        return output.format(oneWayTripDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

