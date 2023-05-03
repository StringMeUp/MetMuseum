package com.sr.metmuseum.base

import androidx.lifecycle.ViewModel
import javax.inject.Inject

open class BaseViewModel @Inject constructor() : ViewModel() {

    sealed class Resource<out T> {
        object Loading : Resource<Nothing>()
        data class Success<out T>(val data: T?) : Resource<T>()
        data class Error(val responseCode: Int) : Resource<Nothing>()
    }
}
