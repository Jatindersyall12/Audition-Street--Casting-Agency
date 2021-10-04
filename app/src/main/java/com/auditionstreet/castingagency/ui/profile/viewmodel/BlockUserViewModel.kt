package com.auditionstreet.castingagency.ui.profile.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.model.response.BlockUserListResponse
import com.auditionstreet.castingagency.ui.profile.repository.BlockUserRepository
import com.auditionstreet.castingagency.ui.profile.repository.ProfileRepository
import com.leo.wikireviews.utils.livedata.Event
import com.silo.model.request.WorkGalleryRequest
import com.silo.utils.network.NetworkHelper
import com.silo.utils.network.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.HashMap

class BlockUserViewModel @ViewModelInject constructor(
    private val blockUserRepository: BlockUserRepository,
    private val networkHelper: NetworkHelper,
) : ViewModel() {

    private val _getBlockUserList = MutableLiveData<Event<Resource<BlockUserListResponse>>>()
    val getBlockUserList: LiveData<Event<Resource<BlockUserListResponse>>>
        get() = _getBlockUserList



    fun getBlockUserList(url: String) {
        viewModelScope.launch {
            _getBlockUserList.postValue(Event(Resource.loading(ApiConstant.GET_BLOCK_ARTIST, null)))
            if (networkHelper.isNetworkConnected()) {
                blockUserRepository.getBlockUserList(url).let {
                    if (it.isSuccessful && it.body() != null) {
                        _getBlockUserList.postValue(
                            Event(
                                Resource.success(
                                    ApiConstant.GET_BLOCK_ARTIST,
                                    it.body()
                                )
                            )
                        )
                    } else {
                        _getBlockUserList.postValue(
                            Event(
                                Resource.error(
                                    ApiConstant.GET_BLOCK_ARTIST,
                                    it.code(),
                                    it.errorBody().toString(),
                                    null
                                )
                            )
                        )
                    }
                }
            }
        }
    }


}