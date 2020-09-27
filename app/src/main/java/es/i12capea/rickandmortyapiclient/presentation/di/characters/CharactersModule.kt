package es.i12capea.rickandmortyapiclient.presentation.di.characters

import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import es.i12capea.rickandmortyapiclient.data.api.CharacterApi
import es.i12capea.rickandmortyapiclient.data.local.dao.LocalCharacterPageDao
import es.i12capea.rickandmortyapiclient.data.repository.CharacterRepositoryImpl
import es.i12capea.rickandmortyapiclient.domain.repositories.CharacterRepository
import es.i12capea.rickandmortyapiclient.domain.usecases.GetCharacterUseCase
import es.i12capea.rickandmortyapiclient.domain.usecases.GetCharactersInPage
import es.i12capea.rickandmortyapiclient.presentation.characters.CharacterListAdapter
import es.i12capea.rickandmortyapiclient.presentation.characters.CharacterListAdapterDeepLink


@Module
@InstallIn(ActivityRetainedComponent::class)
class CharactersModule {


    @Provides
    fun provideCharacterRepository(
        characterApi: CharacterApi,
        characterPageDao: LocalCharacterPageDao
    ) : CharacterRepository{
        return CharacterRepositoryImpl(
            characterApi,
            characterPageDao
        )
    }

    @Provides
    fun provideGetAllCharactersUseCase(charactersRepository: CharacterRepository) : GetCharactersInPage{
        return GetCharactersInPage(charactersRepository)
    }

    @Provides
    fun provideGetCharacterUseCase(charactersRepository: CharacterRepository) : GetCharacterUseCase {
        return GetCharacterUseCase(charactersRepository)
    }

    @Provides
    fun provideCharacterAdapter(requestManager: RequestManager) : CharacterListAdapter {
        return CharacterListAdapter(requestManager)
    }
    @Provides
    fun provideCharacterAdapterDeepLink(requestManager: RequestManager) : CharacterListAdapterDeepLink {
        return CharacterListAdapterDeepLink(requestManager)
    }

}