package com.sr.metmuseum.ui.main

import android.os.Parcelable
import com.sr.metmuseum.R
import kotlinx.parcelize.Parcelize


@Parcelize
data class ArtItem(val id: Int, val type: MainViewModel.ObjectType = MainViewModel.ObjectType.DEFAULT, var isLoading: Boolean = false) :
    Parcelable {
    companion object {
        fun default() = mutableListOf(ArtItem(R.string.default_state))
        fun loading(isLoading: Boolean) = mutableListOf(ArtItem(R.string.loading_state, MainViewModel.ObjectType.LOADING, isLoading))
        fun empty() = mutableListOf(ArtItem(R.string.empty_state, MainViewModel.ObjectType.EMPTY))
        fun error() = mutableListOf(ArtItem(R.string.error_state, MainViewModel.ObjectType.ERROR))
    }
}