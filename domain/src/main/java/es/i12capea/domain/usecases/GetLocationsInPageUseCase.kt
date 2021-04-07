package es.i12capea.domain.usecases

import es.i12capea.domain.entities.LocationEntity
import es.i12capea.domain.entities.PageEntity
import es.i12capea.domain.repositories.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetLocationsInPageUseCase(
    private val locationRepository: LocationRepository
){
    suspend operator fun invoke(page: Int = 1) : Flow<PageEntity<LocationEntity>> {
         return flow {
             locationRepository.getLocationsInPage(page)
                 .flowOn(Dispatchers.IO)
                 .collect {
                     emit(it)
                 }
         }
    }
}