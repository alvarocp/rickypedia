package es.i12capea.rickandmortyapiclient.domain.usecases

import es.i12capea.rickandmortyapiclient.domain.entities.CharacterEntity
import es.i12capea.rickandmortyapiclient.domain.entities.PageEntity
import es.i12capea.rickandmortyapiclient.domain.repositories.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.lang.Exception

class GetCharactersInPage(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(page: Int = 1) : Flow<PageEntity<CharacterEntity>> {
        return flow {
                characterRepository.getCharactersAtPage(page)
                    .flowOn(Dispatchers.IO)
                    .collect {
                        emit(it)
                    }
            }
    }
}