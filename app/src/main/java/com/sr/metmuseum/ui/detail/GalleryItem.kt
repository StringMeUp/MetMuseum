package com.sr.metmuseum.ui.detail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GalleryItem(
    val objectId: Int,
    val primaryImage: String,
    val additionalImages: List<String>? = null,
    val department: String? = null,
    val title: String? = null,
    val culture: String? = null,
    val period: String? = null,
    val artistDisplayName: String? = null,
    val artistBeginDate: String? = null,
    val artistEndDate: String? = null,
    val artistWikidataUrl: String? = null,
    val city: String? = null,
    val type: DetailViewModel.GalleryType = DetailViewModel.GalleryType.MAIN
) : Parcelable