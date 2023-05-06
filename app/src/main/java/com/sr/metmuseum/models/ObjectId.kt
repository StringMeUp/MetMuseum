package com.sr.metmuseum.models

import com.google.gson.annotations.SerializedName

data class ObjectId(
    val total: Int,
    @SerializedName("objectIDs")
    val artIds: List<Int>,
)