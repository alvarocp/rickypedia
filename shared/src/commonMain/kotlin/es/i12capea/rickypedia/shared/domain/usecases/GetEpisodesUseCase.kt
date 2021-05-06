package es.i12capea.rickypedia.shared.domain.usecases

import es.i12capea.rickypedia.shared.domain.entities.EpisodeEntity
import es.i12capea.rickypedia.shared.domain.repositories.EpisodeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class GetEpisodesUseCase(
    private val episodeRepository: EpisodeRepository
) {
    suspend operator fun invoke(episodes: List<Int>) : Flow<List<EpisodeEntity>> {
        return flow {
            episodeRepository.getEpisodes(episodes)
                .flowOn(Dispatchers.Default)
                .collect {
                    emit(it)
                }
        }
    }
}