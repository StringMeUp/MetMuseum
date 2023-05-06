package com.sr.metmuseum.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sr.metmuseum.base.BaseViewModel
import com.sr.metmuseum.repository.MainRepository
import com.sr.metmuseum.util.swap
import com.sr.metmuseum.util.toGalleryItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MainRepository,
) : BaseViewModel() {

    enum class GalleryType {
        MAIN, THUMB
    }

    private var _galleryItems = MutableLiveData<List<GalleryItem>>()
    var galleryItems: LiveData<List<GalleryItem>> = _galleryItems

    private var _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun getItemDetails(itemId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getDetails(itemId)
                .onCompletion { _isLoading.postValue(false) }
                .collect {
                    when (it) {
                        is Resource.Loading -> { _isLoading.postValue(true) }
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
}