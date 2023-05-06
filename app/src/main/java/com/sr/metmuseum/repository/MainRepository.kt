package com.sr.metmuseum.repository

import com.sr.metmuseum.api.Api
import com.sr.metmuseum.util.RemoteSource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepository @Inject constructor(private val api: Api) {

    suspend fun search(q: String) = RemoteSource.launchResultFlow { api.searchObjectIds(q = q) }
    suspend fun getDetails(id: Int) = RemoteSource.launchResultFlow { api.getObjectDetails(id) }
}