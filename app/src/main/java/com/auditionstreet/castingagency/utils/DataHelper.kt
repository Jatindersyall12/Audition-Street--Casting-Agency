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
            R.drawable.first_time_here_1,
            R.drawable.first_time_here_2,
            R.drawable.first_time_here_3,
            R.drawable.first_time_here_4,
            R.drawable.first_time_here_5,
            R.drawable.first_time_here_6,
        )

        var firstTimehereString: ArrayList<String> = arrayListOf(
           "1",
            "2",
            "3",
            "4",
            "5",
            "6"
        )
    }
}