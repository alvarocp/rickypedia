package es.i12capea.data.repository

import android.util.Log
import es.i12capea.data.mappers.toDomain
import es.i12capea.data.api.LocationApi
import es.i12capea.data.local.dao.LocalLocationDao
import es.i12capea.data.local.dao.LocalLocationPageDao
import es.i12capea.rickypedia.data.mappers.*
import es.i12capea.domain.entities.LocationEntity
import es.i12capea.domain.entities.PageEntity
import es.i12capea.domain.repositories.LocationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val api : LocationApi,
    private val locationDao: LocalLocationDao,
    private val locationPageDao: LocalLocationPageDao
) : LocationRepository {

    override suspend  fun getLocationsInPage(page: Int): Flow<PageEntity<LocationEntity>> {
        return flow{
            locationPageDao.searchPageById(page)?.let{
                emit(it.toDomain())
            }
            try {
                val result = api.getLocations(page)
                    .call()

                val locationDomain = result.locationPageToDomain()

                emit(locationDomain)

                Thread{
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            locationDao.insertLocationList(locationDomain.list.listLocationEntityToLocal(locationDomain.actualPage))
                            locationPageDao.insertPage(locationDomain.toLocal())
                        }catch (e: Exception){
                            Log.d("BD", "Error insertando lista de localizaciones")
                        }
                    }
                }.start()
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