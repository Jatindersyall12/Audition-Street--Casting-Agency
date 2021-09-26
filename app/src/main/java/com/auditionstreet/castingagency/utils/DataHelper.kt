package com.auditionstreet.castingagency.utils

import com.auditionstreet.castingagency.R
import javax.inject.Inject

class DataHelper @Inject constructor() {
    companion object {
        var activeIcons: ArrayList<Int> = arrayListOf(
            R.drawable.ic_icon_home_selected,
            R.drawable.ic_icon_project_selected,
            R.drawable.ic_icon_chat_selected,
            R.drawable.ic_icon_account_selected,

        )

        var inActiveIcons: ArrayList<Int> = arrayListOf(
            R.drawable.ic_icon_home_unselected,
            R.drawable.ic_icon_project_unselected,
            R.drawable.ic_icon_chat_unselected,
            R.drawable.ic_icon_account_unselected,
        )

        var firstTimehere: ArrayList<Int> = arrayListOf(
            R.drawable.ic_icon_home_unselected,
            R.drawable.ic_icon_project_unselected,
            R.drawable.ic_icon_chat_unselected,
            R.drawable.ic_icon_account_unselected,
            R.drawable.ic_icon_account_unselected,
            R.drawable.ic_icon_account_unselected,
            R.drawable.ic_icon_account_unselected,
            R.drawable.ic_icon_account_unselected,
            R.drawable.ic_icon_account_unselected,
            R.drawable.ic_icon_account_unselected
        )

        var firstTimehereString: ArrayList<String> = arrayListOf(
           "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10"
        )
    }
}