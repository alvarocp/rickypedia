package es.i12capea.rickypedia.di.episodes

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import es.i12capea.rickypedia.shared.domain.repositories.CharacterRepository
import es.i12capea.rickypedia.shared.domain.repositories.EpisodeRepository
import es.i12capea.rickypedia.shared.domain.usecases.GetCharactersInEpisodeUseCase
import es.i12capea.rickypedia.shared.domain.usecases.GetEpisodeUseCase
import es.i12capea.rickypedia.shared.domain.usecases.GetEpisodesInPageUseCase
import es.i12capea.rickypedia.shared.domain.usecases.GetEpisodesUseCase

@Module
@InstallIn(ActivityRetainedComponent::class)
class EpisodesModule {

    @Provides
    fun provideEpisodeRepository(episodesApi: es.i12capea.data.api.EpisodesApi, episodeDao: es.i12capea.data.local.dao.LocalEpisodeDao, episodesPageDao: es.i12capea.data.local.dao.LocalEpisodePageDao) : EpisodeRepository{
        return es.i12capea.data.repository.EpisodeRepositoryImpl(
            episodesApi,
            episodeDao,
            episodesPageDao
        )
    }

    @Provides
    fun provideGetEpisodesUseCase(episodeRepository: EpisodeRepository) : GetEpisodesUseCase {
        return GetEpisodesUseCase(episodeRepository)
    }

    @Provides
    fun provideGetAllEpisodesUseCase(episodeRepository: EpisodeRepository) : GetEpisodesInPageUseCase {
        return GetEpisodesInPageUseCase(episodeRepository)
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