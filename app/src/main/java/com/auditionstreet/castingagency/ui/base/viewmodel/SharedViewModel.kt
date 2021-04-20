package com.silo.ui.base.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.silo.utils.livedata.SingleLiveEvent

class SharedViewModel @ViewModelInject constructor() : ViewModel(){

    private val _navDirectionLiveData = SingleLiveEvent<NavDirections>()
    val navDirectionLiveData: LiveData<NavDirections> = _navDirectionLiveData

    fun setDirection(navDirections: NavDirections){
        _navDirectionLiveData.value = navDirections
    }
}