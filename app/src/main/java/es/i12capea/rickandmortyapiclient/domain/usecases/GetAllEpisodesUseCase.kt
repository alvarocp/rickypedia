package es.i12capea.rickandmortyapiclient.domain.usecases

import es.i12capea.rickandmortyapiclient.domain.entities.EpisodeEntity
import es.i12capea.rickandmortyapiclient.domain.entities.Page
import es.i12capea.rickandmortyapiclient.domain.repositories.EpisodeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetAllEpisodesUseCase(
    private val episodeRepository: EpisodeRepository
) {
    suspend operator fun invoke(page: Int? = 1) : Flow<List<EpisodeEntity>> {
        return flow {
            episodeRepository.getAllEpisodes(page)
                .flowOn(Dispatchers.IO)
                .collect {
                    emit(it)
                }
        }
    }}