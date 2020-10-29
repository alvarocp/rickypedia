package es.i12capea.rickandmortyapiclient.presentation.di.location

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import es.i12capea.rickandmortyapiclient.data.api.LocationApi
import es.i12capea.rickandmortyapiclient.data.local.dao.LocalLocationDao
import es.i12capea.rickandmortyapiclient.data.local.dao.LocalLocationPageDao
import es.i12capea.rickandmortyapiclient.data.repository.LocationRepositoryImpl
import es.i12capea.rickandmortyapiclient.domain.repositories.CharacterRepository
import es.i12capea.rickandmortyapiclient.domain.repositories.LocationRepository
import es.i12capea.rickandmortyapiclient.domain.usecases.GetCharactersInLocationUseCase
import es.i12capea.rickandmortyapiclient.domain.usecases.GetLocationsInPageUseCase

@Module
@InstallIn(ActivityRetainedComponent::class)
class LocationModule {

    @Provides
    fun provideLocationRepository(
        locationApi: LocationApi,
        localLocationDao: LocalLocationDao,
        localLocationPageDao: LocalLocationPageDao
    ) : LocationRepository{
        return LocationRepositoryImpl(
            locationApi,
            localLocationDao,
            localLocationPageDao
        )
    }

    @Provides
    fun provideGetAllLocationUseCase(locationRepository: LocationRepository) : GetLocationsInPageUseCase{
        return GetLocationsInPageUseCase(locationRepository)
    }

    @Provides
    fun provideGetCharactersForLocation(characterRepository: CharacterRepository) : GetCharactersInLocationUseCase{
        return GetCharactersInLocationUseCase(characterRepository)
    }

}