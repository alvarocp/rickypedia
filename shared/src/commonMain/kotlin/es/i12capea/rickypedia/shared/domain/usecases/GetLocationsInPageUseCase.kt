package es.i12capea.rickypedia.shared.domain.usecases

import es.i12capea.rickypedia.shared.domain.entities.LocationEntity
import es.i12capea.rickypedia.shared.domain.entities.PageEntity
import es.i12capea.rickypedia.shared.domain.repositories.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetLocationsInPageUseCase(
    private val locationRepository: es.i12capea.rickypedia.shared.domain.repositories.LocationRepository
){
    suspend operator fun invoke(page: Int = 1) : Flow<es.i12capea.rickypedia.shared.domain.entities.PageEntity<es.i12capea.rickypedia.shared.domain.entities.LocationEntity>> {
         return flow {
             locationRepository.getLocationsInPage(page)
                 .flowOn(Dispatchers.Default)
                 .collect {
                     emit(it)
                 }
         }
    }
}