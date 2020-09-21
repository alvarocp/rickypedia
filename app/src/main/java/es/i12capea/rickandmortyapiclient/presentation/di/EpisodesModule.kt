package es.i12capea.rickandmortyapiclient.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import es.i12capea.rickandmortyapiclient.data.api.EpisodesApi
import es.i12capea.rickandmortyapiclient.data.repository.EpisodeRepositoryImpl
import es.i12capea.rickandmortyapiclient.domain.repositories.EpisodeRepository
import es.i12capea.rickandmortyapiclient.domain.usecases.GetAllEpisodesUseCase
import es.i12capea.rickandmortyapiclient.domain.usecases.GetEpisodesUseCase

@Module
@InstallIn(ActivityRetainedComponent::class)
class EpisodesModule {


    @Provides
    fun provideEpisodeRepository(episodesApi: EpisodesApi) : EpisodeRepository{
        return EpisodeRepositoryImpl(episodesApi)
    }

    @Provides
    fun provideGetEpisodesUseCase(episodeRepository: EpisodeRepository) : GetEpisodesUseCase {
        return GetEpisodesUseCase(episodeRepository)
    }

    @Provides
    fun provideGetAllEpisodesUseCase(episodeRepository: EpisodeRepository) : GetAllEpisodesUseCase {
        return GetAllEpisodesUseCase(episodeRepository)
    }
}