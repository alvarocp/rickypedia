package es.i12capea.rickandmortyapiclient.domain.usecases

import es.i12capea.rickandmortyapiclient.domain.entities.CharacterEntity
import es.i12capea.rickandmortyapiclient.domain.repositories.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class GetAllCharactersUseCase(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(page: Int = 1) : Flow<List<CharacterEntity>> {
        return flow {
            characterRepository.getAllCharacters(page)
                .flowOn(Dispatchers.IO)
                .collect {
                    emit(it)
                }
        }
    }
}