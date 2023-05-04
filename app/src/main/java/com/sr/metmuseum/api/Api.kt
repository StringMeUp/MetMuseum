package com.sr.metmuseum.api

import com.sr.metmuseum.models.ObjectDetail
import com.sr.metmuseum.models.ObjectId
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("public/collection/v1/search")
    suspend fun searchObjectIds(@Query("q") q: String): Response<ObjectId>

    @GET("public/collection/v1/objects/{objectId}")
    suspend fun getObjectDetails(objectId: String): ObjectDetail
}