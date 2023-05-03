package com.sr.metmuseum.api

import com.sr.metmuseum.models.ObjectDetail
import com.sr.metmuseum.models.ObjectId
import retrofit2.http.GET

interface Api {

    @GET("public/collection/v1/objects")
    suspend fun getObjectIds(): ObjectId

    @GET("public/collection/v1/objects/{objectId}")
    suspend fun getObjectDetails(objectId: String): ObjectDetail
}