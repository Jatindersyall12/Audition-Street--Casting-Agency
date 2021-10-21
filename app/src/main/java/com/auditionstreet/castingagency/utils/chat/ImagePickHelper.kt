package com.auditionstreet.castingagency.utils.chat

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.auditionstreet.castingagency.ui.chat.MediaPickHelperFragment
import com.auditionstreet.castingagency.ui.chat.MediaSourcePickDialogFragment


fun pickAnImage(activity: FragmentActivity, requestCode: Int) {
    val mediaPickHelperFragment = MediaPickHelperFragment.getInstance(activity, requestCode)
    showImageSourcePickerDialog(activity.supportFragmentManager, mediaPickHelperFragment)
}

private fun showImageSourcePickerDialog(fragmentManager: FragmentManager, fragment: MediaPickHelperFragment) {
    MediaSourcePickDialogFragment.show(fragmentManager,
            MediaSourcePickDialogFragment.ImageSourcePickedListener(fragment))
}