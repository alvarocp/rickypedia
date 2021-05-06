package es.i12capea.rickypedia.shared.domain.usecases

import es.i12capea.rickypedia.shared.domain.entities.CharacterEntity
import es.i12capea.rickypedia.shared.domain.entities.LocationEntity
import es.i12capea.rickypedia.shared.domain.repositories.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetCharactersInLocationUseCase(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(location: LocationEntity) : Flow<List<CharacterEntity>> {
        return flow{
            characterRepository.getCharacters(location.residents)
                .flowOn(Dispatchers.Default)
                .collect {
                    emit(it)
                }
        }
    }
}