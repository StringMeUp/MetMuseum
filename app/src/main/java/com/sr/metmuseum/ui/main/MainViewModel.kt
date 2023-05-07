package com.sr.metmuseum.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sr.metmuseum.base.BaseViewModel
import com.sr.metmuseum.repository.MainRepository
import com.sr.metmuseum.ui.detail.GalleryItem
import com.sr.metmuseum.util.Constants
import com.sr.metmuseum.util.swap
import com.sr.metmuseum.util.toGalleryItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : BaseViewModel() {

    enum class ObjectType {
        ART, ERROR, EMPTY, DEFAULT
    }

    enum class GalleryType {
        MAIN, THUMB
    }

    var flow = flowOf<MutableList<ArtItem>>()

    private var _savedQuery = MutableLiveData<String>()
    val savedQuery: LiveData<String> = _savedQuery

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private var _galleryItems = MutableLiveData<List<GalleryItem>>()
    var galleryItems: LiveData<List<GalleryItem>> = _galleryItems

    private var _itemId = MutableLiveData<Int?>()
    val itemId: LiveData<Int?> = _itemId

    private var searchJob: Job? = null

    fun setItemId(id: Int) {
        _itemId.value = id
    }

    fun saveQuery(q: String) {
        _savedQuery.value = q
    }

    fun searchIds(q: String): Flow<MutableList<ArtItem>> {
        return callbackFlow {
            searchJob = viewModelScope.launch {
                repository.search(q)
                    .onCompletion {
                        _isLoading.postValue(false)
                    }
                    .collect {
                        when (it) {
                            is Resource.Loading -> {
                                _isLoading.postValue(true)
                            }
                            is Resource.Success -> {
                                val items = it.data?.artIds?.map { ArtItem(it, ObjectType.ART) }?.toMutableList()
                                trySend(items ?: ArtItem.empty())
                            }
                            is Resource.Error -> {
                                trySend(ArtItem.error())
                            }
                        }
                    }
            }

            awaitClose { searchJob?.cancel(CancellationException("${Constants.DEBUG_INTENDED} Search cancelled.")) }
        }
    }

    fun saveTempFlow(flow: MutableList<ArtItem>){
        this.flow = flowOf(flow)
    }

    /**Detail*/
    fun getItemDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getDetails(id)
                .onCompletion { _isLoading.postValue(false) }
                .collect {
                    when (it) {
                        is Resource.Loading -> {
                            _isLoading.postValue(true)
                        }
                        is Resource.Success -> {
                            val items = it.data?.toGalleryItems()
                            items?.let { _galleryItems.postValue(it) }
                            _error.postValue(items.isNullOrEmpty())
                        }
                        is Resource.Error -> {
                            _error.postValue(true)
                        }
                    }
                }
        }
    }

    fun updateGallery(position: Int) {
        val swapped = galleryItems.value?.toMutableList()?.let {
            val first = it.first()
            val itemAtPosition = it[position]

            it[0] = first.copy(type = GalleryType.THUMB)
            it[position] = itemAtPosition.copy(type = GalleryType.MAIN)
            it.apply { swap(0, position) }
        } ?: emptyList()

        _galleryItems.value = swapped
    }

    fun invalidateGallery() {
        _galleryItems.value = emptyList()
    }

    fun invalidate() {
        invalidateGallery()
        _isLoading.value = false
        _error.value = false
        _itemId.value = null
    }
}