package es.i12capea.rickandmortyapiclient.data.repository

import android.util.Log
import es.i12capea.rickandmortyapiclient.common.Constants
import es.i12capea.rickandmortyapiclient.data.api.EpisodesApi
import es.i12capea.rickandmortyapiclient.data.api.call
import es.i12capea.rickandmortyapiclient.data.local.dao.LocalEpisodeDao
import es.i12capea.rickandmortyapiclient.data.local.dao.LocalEpisodePageDao
import es.i12capea.rickandmortyapiclient.data.mappers.*
import es.i12capea.rickandmortyapiclient.domain.entities.EpisodeEntity
import es.i12capea.rickandmortyapiclient.domain.entities.PageEntity
import es.i12capea.rickandmortyapiclient.domain.repositories.EpisodeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class EpisodeRepositoryImpl @Inject constructor (
    private val episodesApi: EpisodesApi,
    private val episodeDao: LocalEpisodeDao,
    private val episodePageDao: LocalEpisodePageDao
): EpisodeRepository{
    override suspend fun getEpisodesAtPage(page: Int): Flow<PageEntity<EpisodeEntity>> {
        return flow{
            episodePageDao.searchPageById(page)?.let {
                emit(it.toDomain())
                if (it.page.count != Constants.MAX_ITEM_PER_PAGE){
                    emit(retrieveAndSaveEpisodePage(page))
                }
            } ?: kotlin.run {
                emit(retrieveAndSaveEpisodePage(page))
            }

        }
    }

    override suspend fun getEpisodes(episodes: List<Int>): Flow<List<EpisodeEntity>> {
        return flow{
            episodeDao.searchEpisodesByIds(episodes)?.let { localEpisodes ->
                if (localEpisodes.size == episodes.size){
                    emit(localEpisodes.toDomain())
                }else{
                    try {
                        emit(retrieveAndSaveEpisodes(episodes))
                    }catch (t: Throwable){
                        throw t
                    }
                }
            } ?: kotlin.run {
                try {
                    emit(retrieveAndSaveEpisodes(episodes))
                }catch (t: Throwable){
                    throw t
                }
            }
        }
    }

    override suspend fun getEpisode(id: Int): Flow<EpisodeEntity> {
        return flow {
            episodeDao.searchEpisodeById(id)?.let {
                emit(it.toDomain())
            } ?: kotlin.run {
                val result = episodesApi.getEpisode(id)
                .call()

                val episodeEntity = result.toDomain()
                episodeDao.insertEpisode(episodeEntity.toLocal(null))
                emit(episodeEntity)
            }
        }
    }

    suspend fun retrieveAndSaveEpisodePage(page: Int) : PageEntity<EpisodeEntity>{
        try{
            val result = episodesApi.getAllEpisodes(page)
                .call()

            val episodeEntities = result.episodePageToDomain()

            try {
                episodeDao.insertListEpisode(episodeEntities.list.toLocal(episodeEntities.actualPage))
                episodePageDao.insertPage(episodeEntities.toLocalEpisodePage())
            }catch (e: Exception){
                Log.d("BD", "Error insertando lista de episodios")
            }
            return episodeEntities
        }catch (t: Throwable){
            throw t
        }
    }

    suspend fun retrieveAndSaveEpisodes(episodes: List<Int>) : List<EpisodeEntity>{
        val result = episodesApi.getEpisodes(episodes)
            .call()

        val episodesEntities = result.episodesToDomain()

        try {
            episodeDao.insertListEpisode(episodesEntities.toLocal(null))
        }catch (e: Exception){
            Log.d("BD", "Cant Insert list")
        }
        return episodesEntities
    }


}