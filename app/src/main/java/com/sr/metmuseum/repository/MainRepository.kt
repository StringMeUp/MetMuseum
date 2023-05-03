package com.sr.metmuseum.repository

import com.sr.metmuseum.api.Api
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepository @Inject constructor(private val api: Api) {

    suspend fun getIds() = flow { emit(api.getObjectIds()) }
    suspend fun getObjectDetails(id: String) = flow { emit(api.getObjectDetails(id)) }
}