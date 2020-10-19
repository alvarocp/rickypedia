package es.i12capea.rickandmortyapiclient.data.repository

import android.util.Log
import es.i12capea.rickandmortyapiclient.data.api.CharacterApi
import es.i12capea.rickandmortyapiclient.data.api.call
import es.i12capea.rickandmortyapiclient.data.local.dao.LocalCharacterDao
import es.i12capea.rickandmortyapiclient.data.local.dao.LocalCharacterPageDao
import es.i12capea.rickandmortyapiclient.data.local.model.LocalCharacter
import es.i12capea.rickandmortyapiclient.data.mappers.*
import es.i12capea.rickandmortyapiclient.domain.entities.CharacterEntity
import es.i12capea.rickandmortyapiclient.domain.entities.PageEntity
import es.i12capea.rickandmortyapiclient.domain.exceptions.RequestException
import es.i12capea.rickandmortyapiclient.domain.repositories.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepositoryImpl @Inject constructor(
    private val characterApi: CharacterApi,
    private val characterPageDao: LocalCharacterPageDao,
    private val characterDao : LocalCharacterDao
) : CharacterRepository{

    override suspend fun getCharactersAtPage(page: Int): Flow<PageEntity<CharacterEntity>> {
        return flow{
            characterPageDao.searchPageById(page)?.let {
                emit(it.toDomain())
            }
            try {
                val result = characterApi.getCharactersAtPage(page)
                    .call()

                val domainResult = result.characterPageToDomain()
                emit(domainResult)

                val localCharacterPage = domainResult.toLocal()

                try {
                    characterDao.insertListOfCharactersOrReplace(localCharacterPage.characters)
                    characterPageDao.insertPage(localCharacterPage.page)
                }catch (e: Exception){
                    Log.d("BDD", "No se ha podido insertar la página")
                }

            }catch (t: Throwable){
                if (t !is RequestException){
                    throw t
                }
            }
        }
    }

    override suspend fun getCharacters(ids: List<Int>): Flow<List<CharacterEntity>> {
        return flow {
            characterDao.searchCharactersByIds(ids)?.let {
                emit(it.localCharactersToDomain())
            }
            val result = characterApi.getCharacters(ids)
                .call()

            val domainCharacters = result.charactersToDomain()

            emit(domainCharacters)

            coroutineScope {
                characterDao.insertListOfCharactersOrUpdate(
                    domainCharacters.listCharacterEntityToLocal(null)
                )
            }
        }
    }

    override suspend  fun getCharacter(id: Int): Flow<CharacterEntity> {
        return flow {
            characterDao.searchCharacterById(id)?.let {
                emit(it.toDomain())
            }

            val result = characterApi.getCharacter(id)
                .call()

            emit(result.toDomain())
        }
    }
    
}