package com.sr.metmuseum.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sr.metmuseum.base.BaseViewModel
import com.sr.metmuseum.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : BaseViewModel() {

    private var _objIds = MutableLiveData<MutableList<Int>>()
    val objectIds: LiveData<MutableList<Int>> = _objIds

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private var job: Job? = null

    fun searchIds(q: String) {
        _isError.value = false
        job = viewModelScope.launch {
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
                            _objIds.postValue(it.data?.objectIDs)
                        }
                        is Resource.Error -> {
                            _isError.postValue(true)
                        }
                    }
                }
        }
    }

    fun invalidateSearch() {
        _objIds.value = mutableListOf()
        job?.cancel(CancellationException("Search Cancelled"))
    }
}