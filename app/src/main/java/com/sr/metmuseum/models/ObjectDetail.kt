package com.sr.metmuseum.models

import com.google.gson.annotations.SerializedName

data class ObjectDetail(
    @SerializedName("objectID")
    val objectId: Int,
    val primaryImage: String,
    val additionalImages: List<String>?,
    val department: String,
    val objectName: String,
    val title: String,
    val culture: String,
    val period: String,
    val artistDisplayName: String,
    val artistDisplayBio: String,
    val artistBeginDate: String,
    val artistEndDate: String,
    @SerializedName("artistWikidata_URL")
    val artistWikidataUrl: String,
    val city: String,
    val county: String,
)

