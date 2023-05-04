package com.sr.metmuseum.repository

import com.sr.metmuseum.api.Api
import com.sr.metmuseum.util.RemoteSource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepository @Inject constructor(private val api: Api) {

    suspend fun search(q: String) = RemoteSource.launchResultFlow { api.searchObjectIds(q) }
    suspend fun getDetails(id: String) = flow { emit(api.getObjectDetails(id)) }
}