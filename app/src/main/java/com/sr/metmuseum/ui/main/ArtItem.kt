package com.sr.metmuseum.ui.main

import android.os.Parcelable
import com.sr.metmuseum.R
import kotlinx.parcelize.Parcelize


@Parcelize
data class ArtItem(val id: Int, val type: MainViewModel.ObjectType = MainViewModel.ObjectType.DEFAULT):
    Parcelable {
    companion object {
        fun default() = mutableListOf(ArtItem(R.string.default_state))
        fun empty() = mutableListOf(ArtItem(R.string.empty_state, MainViewModel.ObjectType.EMPTY))
        fun error() = mutableListOf(ArtItem(R.string.error_state, MainViewModel.ObjectType.ERROR))
    }
}