package com.sr.metmuseum.ui.main

import android.os.Parcelable
import androidx.lifecycle.*
import com.sr.metmuseum.R
import com.sr.metmuseum.base.BaseViewModel
import com.sr.metmuseum.repository.MainRepository
import com.sr.metmuseum.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.util.concurrent.CancellationException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    enum class ObjectType {
        ART, ERROR, EMPTY, DEFAULT
    }

    private var _artItems = savedStateHandle.getLiveData<MutableList<ArtItem>>(Constants.OBJECT_IDS)
    val artItems: LiveData<MutableList<ArtItem>> = _artItems

    private var _savedQuery = MutableLiveData<String>()
    val savedQuery: LiveData<String> = _savedQuery

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var searchJob: Job? = null

    fun saveQuery(q: String) {
        _savedQuery.value = q
    }

    fun searchIds(q: String) {
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
                            _artItems.postValue(items ?: ArtItem.empty())
                        }
                        is Resource.Error -> {
                            _artItems.postValue(ArtItem.error())
                        }
                    }
                }
        }
    }

    fun setDefaultInvalidate() {
        _artItems.value = ArtItem.default()
        searchJob?.cancel(CancellationException("${Constants.DEBUG_INTENDED} Search cancelled."))
    }
}