package com.auditionstreet.castingagency.customviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class CustomEditText:AppCompatEditText {
    constructor(context: Context?) : super(context!!){setTypeface()}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs){setTypeface()}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr){setTypeface()}

    private fun setTypeface() {
        if (!isInEditMode) {
            super.setTypeface(Typeface.createFromAsset(context.assets, "ProximaNova-Regular.otf"))
        }
    }
}