package com.sr.metmuseum.api

import retrofit2.http.GET

interface Api {

    @GET("public/collection/v1/objects")
    fun getObjectIds(): List<Int>

    @GET("public/collection/v1/objects/{objectId}")
    fun getObjectDetails(objectId: Int)
}