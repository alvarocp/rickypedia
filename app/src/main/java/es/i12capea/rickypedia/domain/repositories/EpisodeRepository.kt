package es.i12capea.rickypedia.domain.repositories

import es.i12capea.rickypedia.domain.entities.EpisodeEntity
import es.i12capea.rickypedia.domain.entities.PageEntity
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {
    suspend fun getEpisodesAtPage(page: Int) : Flow<PageEntity<EpisodeEntity>>
    suspend fun getEpisodes(episodes: List<Int>) : Flow<List<EpisodeEntity>>
    suspend fun getEpisode(id: Int) : Flow<EpisodeEntity>
}