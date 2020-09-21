package es.i12capea.rickandmortyapiclient.data.repository

import android.graphics.pdf.PdfDocument
import es.i12capea.rickandmortyapiclient.data.api.LocationApi
import es.i12capea.rickandmortyapiclient.data.api.call
import es.i12capea.rickandmortyapiclient.data.mappers.locationPageToDomain
import es.i12capea.rickandmortyapiclient.data.mappers.locationsToDomain
import es.i12capea.rickandmortyapiclient.data.mappers.toDomain
import es.i12capea.rickandmortyapiclient.domain.entities.LocationEntity
import es.i12capea.rickandmortyapiclient.domain.entities.PageEntity
import es.i12capea.rickandmortyapiclient.domain.repositories.LocationRepository
import es.i12capea.rickandmortyapiclient.presentation.entities.Page
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    val api : LocationApi
) : LocationRepository {

    override suspend  fun getLocationsInPage(page: Int): Flow<PageEntity<LocationEntity>> {
        return flow{

            try {
                val result = api.getLocations(page)
                    .call()

                emit(result.locationPageToDomain())
            }catch(t: Throwable) {
                throw t
            }

        }
    }

    override suspend  fun getLocation(id: Int): Flow<LocationEntity> {
        return flow{

            val result = api.getLocation(id)

            //emit(result.toDomain())
        }
    }
}