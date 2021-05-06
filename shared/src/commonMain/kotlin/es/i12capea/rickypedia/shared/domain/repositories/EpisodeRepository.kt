package es.i12capea.rickypedia.shared.domain.repositories

import es.i12capea.rickypedia.shared.domain.entities.EpisodeEntity
import es.i12capea.rickypedia.shared.domain.entities.PageEntity
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {
    suspend fun getEpisodesAtPage(page: Int) : Flow<es.i12capea.rickypedia.shared.domain.entities.PageEntity<es.i12capea.rickypedia.shared.domain.entities.EpisodeEntity>>
    suspend fun getEpisodes(episodes: List<Int>) : Flow<List<es.i12capea.rickypedia.shared.domain.entities.EpisodeEntity>>
    suspend fun getEpisode(id: Int) : Flow<es.i12capea.rickypedia.shared.domain.entities.EpisodeEntity>
}