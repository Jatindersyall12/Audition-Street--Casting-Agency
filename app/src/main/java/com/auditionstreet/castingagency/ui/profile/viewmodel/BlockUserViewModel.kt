package com.auditionstreet.castingagency.ui.profile.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.api.ApiConstant
import com.auditionstreet.castingagency.model.response.AddGroupResponse
import com.auditionstreet.castingagency.model.response.BlockUserListResponse
import com.auditionstreet.castingagency.ui.profile.repository.BlockUserRepository
import com.auditionstreet.castingagency.ui.profile.repository.ProfileRepository
import com.leo.wikireviews.utils.livedata.Event
import com.silo.model.request.BlockArtistRequest
import com.silo.model.request.WorkGalleryRequest
import com.silo.utils.network.NetworkHelper
import com.silo.utils.network.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
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

    private val _block_artist = MutableLiveData<Event<Resource<AddGroupResponse>>>()
    val blockArtist: LiveData<Event<Resource<AddGroupResponse>>>
        get() = _block_artist


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

    fun blockArtist(blockArtistRequest: BlockArtistRequest) {
        viewModelScope.launch {
            try {
                withTimeout(ApiConstant.API_TIMEOUT) {
                    _block_artist.postValue(
                        Event(
                            Resource.loading(
                                ApiConstant.BLOCK_ARTIST,
                                null
                            )
                        )
                    )
                    if (networkHelper.isNetworkConnected()) {
                        blockUserRepository.blockArtist(blockArtistRequest).let {
                            if (it.isSuccessful && it.body() != null) {
                                _block_artist.postValue(
                                    Event(
                                        Resource.success(
                                            ApiConstant.BLOCK_ARTIST,
                                            it.body()
                                        )
                                    )
                                )
                            } else {
                                _block_artist.postValue(
                                    Event(
                                        Resource.error(
                                            ApiConstant.BLOCK_ARTIST,
                                            it.code(),
                                            it.errorBody().toString(),
                                            null
                                        )
                                    )
                                )
                            }
                        }
                    } else {
                        _block_artist.postValue(
                            Event(
                                Resource.requiredResource(
                                    ApiConstant.BLOCK_ARTIST,
                                    R.string.err_no_network_available
                                )
                            )
                        )
                    }
                }
            }catch (e: Exception) {
                _block_artist.postValue(
                    Event(
                        Resource.error(
                            ApiConstant.BLOCK_ARTIST,
                            ApiConstant.STATUS_500,
                            "",
                            null
                        )
                    )
                )
            }
        }
    }

}