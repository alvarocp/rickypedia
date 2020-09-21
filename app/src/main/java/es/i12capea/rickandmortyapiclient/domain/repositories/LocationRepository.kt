package es.i12capea.rickandmortyapiclient.domain.repositories

import es.i12capea.rickandmortyapiclient.domain.entities.LocationEntity
import es.i12capea.rickandmortyapiclient.domain.entities.PageEntity
import es.i12capea.rickandmortyapiclient.presentation.entities.Page
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend  fun getLocationsInPage(page: Int = 1) : Flow<PageEntity<LocationEntity>>
    suspend  fun getLocation(id: Int) : Flow<LocationEntity>
}