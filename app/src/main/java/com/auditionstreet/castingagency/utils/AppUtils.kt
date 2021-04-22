package com.silo.utils

import android.app.Activity
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.auditionstreet.castingagency.R
import java.util.ArrayList
import java.util.regex.Pattern

/*private const val EMAIL = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,}"
private const val EMAIL_DOUBLE_DOT = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,}+\\.[A-Za-z]{2,}"*/
private const val USERNAME = "^[a-zA-Z]+[a-zA-Z ]{2,34}"//35
private const val USERID = "^(?=.*[a-zA-Z])[A-Z0-9a-z!@$%*#?&_^+.=-]{2,25}"
private const val MOBILE_NUMBER = "^[0-9]{8,12}"
private const val PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[A-Z0-9a-z!@$%*#?&_^+.=-]{8,35}$"
private const val WEBURL = "((https|http)://)((\\w|-)+)(([.]|[/])((\\w|-)+))+"

private val USERNAME_PATTERN: Pattern = Pattern.compile(USERNAME)
private val USERID_PATTERN: Pattern = Pattern.compile(USERID)
private val MOBILE_NUMBER_PATTERN: Pattern = Pattern.compile(MOBILE_NUMBER)
private val WEBURL_PATTERN: Pattern = Pattern.compile(WEBURL)
private val PASSWORD_PATTERN: Pattern = Pattern.compile(PASSWORD)

fun isValidUserName(target: CharSequence): Boolean {
    return USERNAME_PATTERN.matcher(target).matches()
}


fun String.validUserNameLength(): Boolean {
    return length in 2..34
}

fun isValidUserId(target: CharSequence): Boolean {
    return USERID_PATTERN.matcher(target).matches()
}

fun String.validUserIdLength(): Boolean {
    return length in 2..25
}

fun isValidPassword(target: CharSequence): Boolean {
    return PASSWORD_PATTERN.matcher(target).matches()
}

fun isValidEmail(target: CharSequence): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(target).matches()
}

fun String.validPhoneLength(): Boolean {
    return length in 8..12
}

fun isValidMobileNumber(target: CharSequence): Boolean {
    return MOBILE_NUMBER_PATTERN.matcher(target).matches()
}

fun String.validPasswordLength(): Boolean {
    return length in 8..35
}

fun isValidWebUrl(target: CharSequence): Boolean {
    return WEBURL_PATTERN.matcher(target).matches()
}

fun setMaxLength(editText: EditText?, length: Int){
    if(editText !=null) {
        editText.filters = arrayOf<InputFilter>(LengthFilter(length))
        editText.setText("")
    }
}

fun isTextEmpty(target: String): String {
    return if(!TextUtils.isEmpty(target)) target else ""
}

fun changeIcons(
    imageButton: ArrayList<ImageView>, activeImage: ArrayList<Int>,
    inActiveImage: ArrayList<Int>,
    position: Int,
    bottomBarText: ArrayList<TextView>,
    homeActivity: Activity
) {
    for (k in 0 until imageButton.size) {
        if (position == k) {

            imageButton[k].setImageResource(activeImage[k])
            bottomBarText[k].setTextColor(ContextCompat.getColor(homeActivity, R.color.orange))
        } else {
            bottomBarText[k].setTextColor(ContextCompat.getColor(homeActivity, R.color.gray))
            imageButton[k].setImageResource(inActiveImage[k])
        }
    }
}