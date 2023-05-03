package com.sr.metmuseum.ui

import androidx.lifecycle.viewModelScope
import com.sr.metmuseum.base.BaseViewModel
import com.sr.metmuseum.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : BaseViewModel() {

    fun getIds() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getIds()
                .catch { Timber.d(it) }
                .collectLatest {
                    Timber.d("${it.total}")
                }
        }
    }
}