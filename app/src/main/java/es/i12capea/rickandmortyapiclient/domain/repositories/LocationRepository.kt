package es.i12capea.rickandmortyapiclient.domain.repositories

import es.i12capea.rickandmortyapiclient.domain.entities.LocationEntity
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend  fun getAllLocations(page: Int? = null) : Flow<List<LocationEntity>>
    suspend  fun getLocation(id: Int) : Flow<LocationEntity>
}