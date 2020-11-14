package es.i12capea.rickypedia.presentation.di.location

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import es.i12capea.rickypedia.data.api.LocationApi
import es.i12capea.rickypedia.data.local.dao.LocalLocationDao
import es.i12capea.rickypedia.data.local.dao.LocalLocationPageDao
import es.i12capea.rickypedia.data.repository.LocationRepositoryImpl
import es.i12capea.rickypedia.domain.repositories.CharacterRepository
import es.i12capea.rickypedia.domain.repositories.LocationRepository
import es.i12capea.rickypedia.domain.usecases.GetCharactersInLocationUseCase
import es.i12capea.rickypedia.domain.usecases.GetLocationsInPageUseCase

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