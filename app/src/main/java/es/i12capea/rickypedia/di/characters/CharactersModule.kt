package es.i12capea.rickypedia.di.characters

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import es.i12capea.data.api.CharacterApi
import es.i12capea.data.local.dao.LocalCharacterDao
import es.i12capea.data.local.dao.LocalCharacterPageDao
import es.i12capea.data.repository.CharacterRepositoryImpl
import es.i12capea.domain.repositories.CharacterRepository
import es.i12capea.domain.usecases.GetCharacterUseCase
import es.i12capea.domain.usecases.GetCharactersInPageUseCase


@Module
@InstallIn(ActivityRetainedComponent::class)
class CharactersModule {

    @Provides
    fun provideCharacterRepository(
        characterApi: CharacterApi,
        localCharacterPageDao: LocalCharacterPageDao,
        localCharacterDao: LocalCharacterDao
    ) : CharacterRepository{
        return CharacterRepositoryImpl(
            characterApi,
            localCharacterPageDao,
            localCharacterDao
        )
    }

    @Provides
    fun provideGetAllCharactersUseCase(charactersRepository: CharacterRepository) : GetCharactersInPageUseCase{
        return GetCharactersInPageUseCase(charactersRepository)
    }

    @Provides
    fun provideGetCharacterUseCase(charactersRepository: CharacterRepository) : GetCharacterUseCase {
        return GetCharacterUseCase(charactersRepository)
    }

}