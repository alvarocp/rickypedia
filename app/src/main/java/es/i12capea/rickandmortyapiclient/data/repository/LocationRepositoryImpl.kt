package es.i12capea.rickandmortyapiclient.data.repository

import es.i12capea.rickandmortyapiclient.data.api.LocationApi
import es.i12capea.rickandmortyapiclient.data.mappers.locationsToDomain
import es.i12capea.rickandmortyapiclient.data.mappers.toDomain
import es.i12capea.rickandmortyapiclient.domain.entities.LocationEntity
import es.i12capea.rickandmortyapiclient.domain.repositories.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    val api : LocationApi
) : LocationRepository {

    override suspend  fun getAllLocations(page: Int?): Flow<List<LocationEntity>> {
        return flow{

            val result = api.getLocations(page)

            //emit(result.results.locationsToDomain())
        }
    }

    override suspend  fun getLocation(id: Int): Flow<LocationEntity> {
        return flow{

            val result = api.getLocation(id)

            //emit(result.toDomain())
        }
    }
}