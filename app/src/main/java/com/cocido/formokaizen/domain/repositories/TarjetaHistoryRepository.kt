package com.cocido.formokaizen.domain.repositories

import com.cocido.formokaizen.domain.entities.TarjetaActivityFeed
import com.cocido.formokaizen.domain.entities.TarjetaHistory
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TarjetaHistoryRepository {
    suspend fun getTarjetaHistory(tarjetaId: Int, page: Int = 0, size: Int = 20): Flow<Resource<List<TarjetaHistory>>>
    suspend fun getTarjetaActivityFeed(tarjetaId: Int, page: Int = 0, size: Int = 20): Flow<Resource<TarjetaActivityFeed>>
    suspend fun getMyActivityFeed(page: Int = 0, size: Int = 20): Flow<Resource<List<TarjetaHistory>>>
    suspend fun getTeamActivityFeed(page: Int = 0, size: Int = 20): Flow<Resource<List<TarjetaHistory>>>
}