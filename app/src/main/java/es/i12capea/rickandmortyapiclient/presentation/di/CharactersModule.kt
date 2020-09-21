package es.i12capea.rickandmortyapiclient.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import es.i12capea.rickandmortyapiclient.data.api.CharacterApi
import es.i12capea.rickandmortyapiclient.data.repository.CharacterRepositoryImpl
import es.i12capea.rickandmortyapiclient.domain.repositories.CharacterRepository
import es.i12capea.rickandmortyapiclient.domain.usecases.GetCharacterUseCase
import es.i12capea.rickandmortyapiclient.domain.usecases.GetCharactersInPage


@Module
@InstallIn(ActivityRetainedComponent::class)
class CharactersModule {


    @Provides
    fun provideCharacterRepository(characterApi: CharacterApi) : CharacterRepository{
        return CharacterRepositoryImpl(characterApi)
    }

    @Provides
    fun provideGetAllCharactersUseCase(charactersRepository: CharacterRepository) : GetCharactersInPage{
        return GetCharactersInPage(charactersRepository)
    }

    @Provides
    fun provideGetCharacterUseCase(charactersRepository: CharacterRepository) : GetCharacterUseCase {
        return GetCharacterUseCase(charactersRepository)
    }

}