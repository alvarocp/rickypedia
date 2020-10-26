package es.i12capea.rickandmortyapiclient.presentation.di.location

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import es.i12capea.rickandmortyapiclient.data.api.LocationApi
import es.i12capea.rickandmortyapiclient.data.repository.LocationRepositoryImpl
import es.i12capea.rickandmortyapiclient.domain.repositories.CharacterRepository
import es.i12capea.rickandmortyapiclient.domain.repositories.LocationRepository
import es.i12capea.rickandmortyapiclient.domain.usecases.GetCharactersInLocationUseCase
import es.i12capea.rickandmortyapiclient.domain.usecases.GetLocationsInPageUseCase

@Module
@InstallIn(ActivityRetainedComponent::class)
class LocationModule {

    @Provides
    fun provideLocationRepository(locationApi: LocationApi) : LocationRepository{
        return LocationRepositoryImpl(locationApi)
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