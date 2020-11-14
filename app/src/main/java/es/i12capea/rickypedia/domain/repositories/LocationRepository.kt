package es.i12capea.rickypedia.domain.repositories

import es.i12capea.rickypedia.domain.entities.LocationEntity
import es.i12capea.rickypedia.domain.entities.PageEntity
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend  fun getLocationsInPage(page: Int = 1) : Flow<PageEntity<LocationEntity>>
    suspend  fun getLocation(id: Int) : Flow<LocationEntity>
}