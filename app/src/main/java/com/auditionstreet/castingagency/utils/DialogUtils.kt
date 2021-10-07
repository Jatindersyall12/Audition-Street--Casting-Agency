@file:Suppress("DEPRECATION")

package com.auditionstreet.castingagency.utils

import android.app.ActionBar
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.customviews.CustomButton
import com.auditionstreet.castingagency.customviews.CustomEditText
import com.auditionstreet.castingagency.customviews.CustomTextView
import com.auditionstreet.castingagency.customviews.CustomTextViewBold
import com.auditionstreet.castingagency.model.BodyTypeModel
import com.auditionstreet.castingagency.model.LanguageModel
import com.auditionstreet.castingagency.model.response.AllAdminResponse
import com.auditionstreet.castingagency.model.response.AllUsersResponse
import com.auditionstreet.castingagency.model.response.GetBodyTypeLanguageResponse
import com.auditionstreet.castingagency.model.response.MyProjectResponse
import com.auditionstreet.castingagency.ui.home.adapter.SelectProjectListAdapter
import com.auditionstreet.castingagency.ui.projects.adapter.*
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.LoadControl
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultAllocator
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.all_application_item.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun showImageOrVideoDialog(
    mContext: Context, url: String, isImage: Boolean
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
    val playerView = dialogView.findViewById<PlayerView>(R.id.player_view)
    if (isImage) {
        Glide.with(mContext).load(url)
            .into(imgPopUp)
        imgPopUp.visibility = View.VISIBLE
    } else {
        playerView.visibility = View.VISIBLE
        playVideo(mContext, playerView, url)
    }

    dialogView.setOnDismissListener {
        if (!isImage) {
            playerView.player!!.playWhenReady = false
            playerView.player!!.stop()
            playerView.player!!.release()
            playerView.player = null
        }
    }
  //  val width = (mContext.getResources().getDisplayMetrics().widthPixels * 0.90)
   // val height = 750
    dialogView.getWindow()!!.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
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

fun showDeleteDialog(
    mContext: Activity,
    mCallback: (pos: Int) -> Unit
): Dialog {
    val dialogView = Dialog(mContext)
    dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
    val binding =
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(mContext),
            R.layout.popup_delete,
            null,
            false
        )
    dialogView.setContentView(binding.root)
    dialogView.setCancelable(false)
    dialogView.show()
    val tvYes = dialogView.findViewById<CustomTextView>(R.id.tvYes)
    val tvNo = dialogView.findViewById<CustomTextView>(R.id.tvNo)
    tvYes.setOnClickListener {
        mCallback.invoke(1)
        dialogView.dismiss()
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
    val tvImages = dialogView.findViewById<CustomTextViewBold>(R.id.tvChooseImages)
    val tvVides = dialogView.findViewById<CustomTextViewBold>(R.id.tvChooseVideos)
    val tvCancel = dialogView.findViewById<CustomTextViewBold>(R.id.tvCancel)

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

fun showVideoOrImageValidation(
    mContext: Context,
    errorString: String,

    ): Dialog {
    val dialogView = Dialog(mContext)
    dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
    val binding =
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(mContext),
            R.layout.popup_video_image_error,
            null,
            false
        )
    dialogView.setContentView(binding.root)
    dialogView.setCancelable(false)
    dialogView.show()
    val tvVideo = dialogView.findViewById<CustomTextView>(R.id.tvVideo)
    val tvOk = dialogView.findViewById<CustomTextViewBold>(R.id.tvOk)
    tvVideo.setText(errorString)
    tvOk.setOnClickListener {
        dialogView.dismiss()
    }

    val width = (mContext.getResources().getDisplayMetrics().widthPixels * 0.90)
    val height = 570
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
    val etSearch = dialogView.findViewById<CustomEditText>(R.id.etSearch)

    etSearch.addTextChangedListener(object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            // TODO Auto-generated method stub
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            // TODO Auto-generated method stub
        }

        override fun afterTextChanged(s: Editable) {

            // filter your list from your input
            val temp: ArrayList<AllUsersResponse.Data> = ArrayList()
            for (d in allUserResponse.data) {
                //or use .equal(text) with you want equal match
                //use .toLowerCase() for better matches
                if (d.name.toLowerCase().contains(s.toString().toLowerCase())) {
                    temp.add(d)
                }
            }
            //update recyclerview
            rvUserAdapter.submitList(temp)
            //you can use runnable postDelayed like 500 ms to delay search text
        }
    })

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
    mCallback: (mProjectId: String) -> Unit
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
    dialogView.setCancelable(true)
    val rvAllUser = dialogView.findViewById<RecyclerView>(R.id.rvAdmin)
    val tvNoRecord = dialogView.findViewById<CustomTextView>(R.id.tvNoRecord)

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
            dialogView.cancel()
            mCallback.invoke(projectId)
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

fun processVideo(uri: Uri?, context: Context, mCallback: (path: String) -> Unit) {
    lateinit var path: String
    val showProgressDialog = showProgressDialog(context)
    val progressBar = showProgressDialog.findViewById<ProgressBar>(R.id.progressbar)

    uri?.let {

        GlobalScope.launch {
            // run in background as it can take a long time if the video is big,
            // this implementation is not the best way to do it,
            // todo(abed): improve threading
            val job = async { getMediaPath(context, uri) }
            path = job.await()

            val desFile = saveVideoFile(path, context)

            desFile?.let {
                var time = 0L
                VideoCompressor.start(
                    context = context,
                    srcUri = uri,
                    // srcPath = path,
                    destPath = desFile.path,
                    listener = object : CompressionListener {
                        override fun onProgress(percent: Float) {
                            if (percent <= 100 && percent.toInt() % 5 == 0) {
                                progressBar.setProgress(percent.toInt())
                            }
                        }

                        override fun onStart() {

                        }

                        override fun onSuccess() {
                            path = desFile.path
                            showProgressDialog.dismiss()
                            mCallback.invoke(path)

                        }

                        override fun onFailure(failureMessage: String) {
                            // progress.text = failureMessage
                            Log.wtf("failureMessage", failureMessage)
                        }

                        override fun onCancelled() {
                            Log.wtf("TAG", "compression has been cancelled")
                            // make UI changes, cleanup, etc
                        }
                    },
                    quality = VideoQuality.MEDIUM,
                    isMinBitRateEnabled = false,
                    keepOriginalResolution = false,
                )
            }
        }
    }
}

@Suppress("DEPRECATION")
private fun saveVideoFile(filePath: String?, context: Context): File? {
    filePath?.let {
        val videoFile = File(filePath)
        val videoFileName = "${System.currentTimeMillis()}_${videoFile.name}"
        val folderName = Environment.DIRECTORY_MOVIES
        if (Build.VERSION.SDK_INT >= 30) {

            val values = ContentValues().apply {

                put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    videoFileName
                )
                put(MediaStore.Images.Media.MIME_TYPE, "video/mp4")
                put(MediaStore.Images.Media.RELATIVE_PATH, folderName)
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }

            val collection =
                MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

            val fileUri = context.contentResolver.insert(collection, values)

            fileUri?.let {
                context.contentResolver.openFileDescriptor(fileUri, "rw")
                    .use { descriptor ->
                        descriptor?.let {
                            FileOutputStream(descriptor.fileDescriptor).use { out ->
                                FileInputStream(videoFile).use { inputStream ->
                                    val buf = ByteArray(4096)
                                    while (true) {
                                        val sz = inputStream.read(buf)
                                        if (sz <= 0) break
                                        out.write(buf, 0, sz)
                                    }
                                }
                            }
                        }
                    }

                values.clear()
                values.put(MediaStore.Video.Media.IS_PENDING, 0)
                context.contentResolver.update(fileUri, values, null, null)

                return File(getMediaPath(context, fileUri))
            }
        } else {
            val downloadsPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val desFile = File(downloadsPath, videoFileName)

            if (desFile.exists())
                desFile.delete()

            try {
                desFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return desFile
        }
    }
    return null
}

fun getMediaPath(context: Context, uri: Uri): String {

    val resolver = context.contentResolver
    val projection = arrayOf(MediaStore.Video.Media.DATA)
    var cursor: Cursor? = null
    try {
        cursor = resolver.query(uri, projection, null, null, null)
        return if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(columnIndex)

        } else ""

    } catch (e: Exception) {
        resolver.let {
            val filePath = (context.applicationInfo.dataDir + File.separator
                    + System.currentTimeMillis())
            val file = File(filePath)

            resolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    val buf = ByteArray(4096)
                    var len: Int
                    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(
                        buf,
                        0,
                        len
                    )
                }
            }
            return file.absolutePath
        }
    } finally {
        cursor?.close()
    }
}

fun playVideo(mContext: Context, playerView: PlayerView, url: String) {
    Log.e("url", url)
    var proxyUrl = url
    val loadControl: LoadControl = DefaultLoadControl.Builder()
        .setAllocator(DefaultAllocator(true, 16))
        .setBufferDurationsMs(1 * 1024, 1 * 1024, 500, 1024)
        .setTargetBufferBytes(-1)
        .setPrioritizeTimeOverSizeThresholds(true)
        .createDefaultLoadControl()
    val trackSelector = DefaultTrackSelector()
    var player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl)
    val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
        mContext,
        Util.getUserAgent(mContext, mContext.getResources().getString(R.string.app_name))
    )
    val videoSource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
        .createMediaSource(Uri.parse(proxyUrl))

    playerView.setPlayer(player)
    player.seekTo(0, 0)
    player.setRepeatMode(Player.REPEAT_MODE_ALL)
    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH)
    player.prepare(videoSource)
    player.setPlayWhenReady(true)
}

fun showProgressDialog(
    mContext: Context
): Dialog {
    val dialogView = Dialog(mContext)
    dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
    val binding =
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(mContext),
            R.layout.pop_up_progress,
            null,
            false
        )
    dialogView.setContentView(binding.root)
    dialogView.setCancelable(false)
    dialogView.show()
    val width = (mContext.getResources().getDisplayMetrics().widthPixels * 0.70)
    val height = 370
    dialogView.getWindow()!!.setLayout(width.toInt(), height.toInt())
    return dialogView
}

fun showLanguageSelectionDialog(
    mContext: Context,
    languageList: ArrayList<GetBodyTypeLanguageResponse.Data.Language>,
    mCallback: (year: String) -> Unit
): Dialog {
    lateinit var rvLanguageAdapter: LanguageListAdapter
    val dialogView = Dialog(mContext)
    dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
    val binding =
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(mContext),
            R.layout.popup_language,
            null,
            false
        )
    dialogView.setContentView(binding.root)
    dialogView.setCancelable(false)
    val rvLanguage = dialogView.findViewById<RecyclerView>(R.id.rvLanguage)
    val btnDone = dialogView.findViewById<CustomButton>(R.id.btnDone)
    val tvNoRecord = dialogView.findViewById<CustomTextView>(R.id.tvNoRecord)

    btnDone.setOnClickListener {
        dialogView.cancel()
        mCallback.invoke("Sd")
    }
    if (languageList.size > 0) {
        rvLanguage.visibility = View.VISIBLE
        tvNoRecord.visibility = View.GONE
    } else {
        rvLanguage.visibility = View.GONE
        tvNoRecord.visibility = View.VISIBLE
    }
    rvLanguage.apply {
        layoutManager = LinearLayoutManager(mContext)
        rvLanguageAdapter = LanguageListAdapter(mContext as FragmentActivity)
        { projectId: String ->

        }
        adapter = rvLanguageAdapter
        rvLanguageAdapter.submitList(languageList)
    }

    dialogView.show()
    val width = (mContext.getResources().getDisplayMetrics().widthPixels * 0.90)
    val height = (mContext.getResources().getDisplayMetrics().heightPixels * 0.65)
    dialogView.getWindow()!!.setLayout(width.toInt(), height.toInt())
    return dialogView
}

fun showBodyTypeSelectionDialog(
    mContext: Context,
    bodyTypeList: ArrayList<GetBodyTypeLanguageResponse.Data.BodyType>,
    mCallback: (year: String) -> Unit
): Dialog {
    lateinit var rvBodyTypeAdapter: BodyTypeListAdapter
    val dialogView = Dialog(mContext)
    dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
    val binding =
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(mContext),
            R.layout.body_type_language,
            null,
            false
        )
    dialogView.setContentView(binding.root)
    dialogView.setCancelable(false)
    val rvBodyType = dialogView.findViewById<RecyclerView>(R.id.rvBodyType)
    val btnDone = dialogView.findViewById<CustomButton>(R.id.btnDone)
    val tvNoRecord = dialogView.findViewById<CustomTextView>(R.id.tvNoRecord)

    btnDone.setOnClickListener {
        dialogView.cancel()
        mCallback.invoke("Sd")
    }
    if (bodyTypeList.size > 0) {
        rvBodyType.visibility = View.VISIBLE
        tvNoRecord.visibility = View.GONE
    } else {
        rvBodyType.visibility = View.GONE
        tvNoRecord.visibility = View.VISIBLE
    }
    rvBodyType.apply {
        layoutManager = LinearLayoutManager(mContext)
        rvBodyTypeAdapter = BodyTypeListAdapter(mContext as FragmentActivity)
        { projectId: String ->

        }
        adapter = rvBodyTypeAdapter
        rvBodyTypeAdapter.submitList(bodyTypeList)
    }

    dialogView.show()
    val width = (mContext.getResources().getDisplayMetrics().widthPixels * 0.90)
    val height = (mContext.getResources().getDisplayMetrics().heightPixels * 0.65)
    dialogView.getWindow()!!.setLayout(width.toInt(), height.toInt())
    return dialogView
}


fun showSkinToneSelectionDialog(
    mContext: Context,
    bodyTypeList: ArrayList<GetBodyTypeLanguageResponse.Data.SkinTone>,
    mCallback: (year: String) -> Unit
): Dialog {
    lateinit var rvBodyTypeAdapter: SkinToneListAdapter
    val dialogView = Dialog(mContext)
    dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
    val binding =
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(mContext),
            R.layout.popup_skin_tone,
            null,
            false
        )
    dialogView.setContentView(binding.root)
    dialogView.setCancelable(false)
    val rvBodyType = dialogView.findViewById<RecyclerView>(R.id.rvSkinTone)
    val btnDone = dialogView.findViewById<CustomButton>(R.id.btnDone)
    val tvNoRecord = dialogView.findViewById<CustomTextView>(R.id.tvNoRecord)

    btnDone.setOnClickListener {
        dialogView.cancel()
        mCallback.invoke("Sd")
    }
    if (bodyTypeList.size > 0) {
        rvBodyType.visibility = View.VISIBLE
        tvNoRecord.visibility = View.GONE
    } else {
        rvBodyType.visibility = View.GONE
        tvNoRecord.visibility = View.VISIBLE
    }
    rvBodyType.apply {
        layoutManager = LinearLayoutManager(mContext)
        rvBodyTypeAdapter = SkinToneListAdapter(mContext as FragmentActivity)
        { projectId: String ->

        }
        adapter = rvBodyTypeAdapter
        rvBodyTypeAdapter.submitList(bodyTypeList)
    }

    dialogView.show()
    val width = (mContext.getResources().getDisplayMetrics().widthPixels * 0.90)
    val height = (mContext.getResources().getDisplayMetrics().heightPixels * 0.65)
    dialogView.getWindow()!!.setLayout(width.toInt(), height.toInt())
    return dialogView
}
