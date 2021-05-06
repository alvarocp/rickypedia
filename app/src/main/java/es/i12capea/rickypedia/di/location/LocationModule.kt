package es.i12capea.rickypedia.di.location

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import es.i12capea.rickypedia.shared.domain.repositories.CharacterRepository
import es.i12capea.rickypedia.shared.domain.repositories.LocationRepository
import es.i12capea.rickypedia.shared.domain.usecases.GetCharactersInLocationUseCase
import es.i12capea.rickypedia.shared.domain.usecases.GetLocationsInPageUseCase

@Module
@InstallIn(ActivityRetainedComponent::class)
class LocationModule {

    @Provides
    fun provideLocationRepository(
        locationApi: es.i12capea.data.api.LocationApi,
        localLocationDao: es.i12capea.data.local.dao.LocalLocationDao,
        localLocationPageDao: es.i12capea.data.local.dao.LocalLocationPageDao
    ) : LocationRepository{
        return es.i12capea.data.repository.LocationRepositoryImpl(
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