package es.i12capea.domain.usecases

import es.i12capea.domain.entities.CharacterEntity
import es.i12capea.domain.entities.EpisodeEntity
import es.i12capea.domain.repositories.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetCharactersInEpisodeUseCase(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(episode: EpisodeEntity) : Flow<List<CharacterEntity>> {
        return flow {
            characterRepository.getCharacters(episode.characters)
                .flowOn(Dispatchers.IO)
                .collect {
                    emit(it)
                }
        }
    }
}