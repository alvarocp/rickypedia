package es.i12capea.rickandmortyapiclient.domain.usecases

import es.i12capea.rickandmortyapiclient.domain.entities.EpisodeEntity
import es.i12capea.rickandmortyapiclient.domain.repositories.EpisodeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetEpisodeUseCase(
    private val episodeRepository: EpisodeRepository
) {
    suspend operator fun invoke(id: Int) : Flow<EpisodeEntity> {
        return flow {
            episodeRepository.getEpisode(id)
                .flowOn(Dispatchers.IO)
                .collect { emit(it) }
        }
    }
}