package es.i12capea.rickandmortyapiclient.domain.usecases

import es.i12capea.rickandmortyapiclient.domain.entities.CharacterEntity
import es.i12capea.rickandmortyapiclient.domain.entities.PageEntity
import es.i12capea.rickandmortyapiclient.domain.repositories.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetCharactersInPageUseCase(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(page: Int) : Flow<PageEntity<CharacterEntity>> {
        return flow {
                characterRepository.getCharactersAtPage(page)
                    .flowOn(Dispatchers.IO)
                    .collect {
                        emit(it)
                    }
            }
    }
}