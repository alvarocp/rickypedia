package es.i12capea.rickandmortyapiclient.domain.repositories

import es.i12capea.rickandmortyapiclient.domain.entities.EpisodeEntity
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {
    suspend fun getAllEpisodes(page: Int? = null) : Flow<List<EpisodeEntity>>
    suspend fun getEpisodes(episodes: List<Int>) : Flow<List<EpisodeEntity>>
    suspend fun getEpisode(id: Int) : Flow<EpisodeEntity>
}