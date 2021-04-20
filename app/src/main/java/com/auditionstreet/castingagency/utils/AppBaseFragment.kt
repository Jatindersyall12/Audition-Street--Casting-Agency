package com.silo.utils

import com.silo.ui.base.BaseFragment
import com.silo.listeners.DialogHelper
import com.silo.listeners.DialogProvider
import java.lang.UnsupportedOperationException

abstract class AppBaseFragment(layoutResId: Int) : BaseFragment(layoutResId){

    override fun provideDialogHelper(): DialogHelper {
        val requireActivity = requireActivity()
        if(requireActivity is DialogProvider)
            return requireActivity.provideDialogHelper()
        throw UnsupportedOperationException()
    }
}