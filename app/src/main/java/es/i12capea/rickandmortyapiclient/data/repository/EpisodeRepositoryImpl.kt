package es.i12capea.rickandmortyapiclient.data.repository

import es.i12capea.rickandmortyapiclient.data.api.EpisodesApi
import es.i12capea.rickandmortyapiclient.data.api.call
import es.i12capea.rickandmortyapiclient.data.mappers.episodesToDomain
import es.i12capea.rickandmortyapiclient.data.mappers.toDomain
import es.i12capea.rickandmortyapiclient.domain.entities.EpisodeEntity
import es.i12capea.rickandmortyapiclient.domain.repositories.EpisodeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class EpisodeRepositoryImpl @Inject constructor (
    private val episodesApi: EpisodesApi
): EpisodeRepository{
    override suspend fun getAllEpisodes(page: Int?): Flow<List<EpisodeEntity>> {
        return flow{

            val result = episodesApi.getAllEpisodes(page)
                .call()

            val episodeEntities = result.results.episodesToDomain()

            emit(episodeEntities)
        }
    }

    override suspend fun getEpisodes(episodes: List<Int>): Flow<List<EpisodeEntity>> {
        return flow{

            val result = episodesApi.getEpisodes(episodes)
                .call()

            val episodeEntities = result.episodesToDomain()

            emit(episodeEntities)
        }
    }

    override suspend fun getEpisode(id: Int): Flow<EpisodeEntity> {
        return flow {
            val result = episodesApi.getEpisode(id)
                .call()

            val episodeEntity = result.toDomain()
            emit(episodeEntity)
        }
    }
}