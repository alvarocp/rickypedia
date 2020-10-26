package es.i12capea.rickandmortyapiclient.presentation.di.episodes

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import es.i12capea.rickandmortyapiclient.data.api.EpisodesApi
import es.i12capea.rickandmortyapiclient.data.local.dao.LocalEpisodeDao
import es.i12capea.rickandmortyapiclient.data.local.dao.LocalEpisodePageDao
import es.i12capea.rickandmortyapiclient.data.repository.EpisodeRepositoryImpl
import es.i12capea.rickandmortyapiclient.domain.repositories.CharacterRepository
import es.i12capea.rickandmortyapiclient.domain.repositories.EpisodeRepository
import es.i12capea.rickandmortyapiclient.domain.usecases.GetAllEpisodesUseCase
import es.i12capea.rickandmortyapiclient.domain.usecases.GetCharactersInEpisodeUseCase
import es.i12capea.rickandmortyapiclient.domain.usecases.GetEpisodeUseCase
import es.i12capea.rickandmortyapiclient.domain.usecases.GetEpisodesUseCase

@Module
@InstallIn(ActivityRetainedComponent::class)
class EpisodesModule {

    @Provides
    fun provideEpisodeRepository(episodesApi: EpisodesApi, episodeDao: LocalEpisodeDao, episodesPageDao: LocalEpisodePageDao ) : EpisodeRepository{
        return EpisodeRepositoryImpl(episodesApi, episodeDao, episodesPageDao)
    }

    @Provides
    fun provideGetEpisodesUseCase(episodeRepository: EpisodeRepository) : GetEpisodesUseCase {
        return GetEpisodesUseCase(episodeRepository)
    }

    @Provides
    fun provideGetAllEpisodesUseCase(episodeRepository: EpisodeRepository) : GetAllEpisodesUseCase {
        return GetAllEpisodesUseCase(episodeRepository)
    }

    @Provides
    fun provideGetCharactersInEpisodeUseCase(characterRepository: CharacterRepository) : GetCharactersInEpisodeUseCase{
        return GetCharactersInEpisodeUseCase(characterRepository)
    }

    @Provides
    fun provideGetEpisodeUseCase(episodeRepository: EpisodeRepository) : GetEpisodeUseCase{
        return GetEpisodeUseCase(episodeRepository)
    }

}