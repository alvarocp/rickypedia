package es.i12capea.data.repository

import android.util.Log
import es.i12capea.data.api.CharacterApi
import es.i12capea.data.local.dao.LocalCharacterDao
import es.i12capea.data.local.dao.LocalCharacterPageDao
import es.i12capea.data.mappers.*
import es.i12capea.rickypedia.data.mappers.*
import es.i12capea.domain.entities.CharacterEntity
import es.i12capea.domain.entities.PageEntity
import es.i12capea.domain.exceptions.RequestException
import es.i12capea.domain.repositories.CharacterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
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
                if (it.page.count == it.characters.size){
                    emit(it.toDomain())
                }
            }
            try {
                val result = characterApi.getCharactersAtPage(page)
                    .call()

                val domainResult = result.characterPageToDomain()
                emit(domainResult)

                Thread{
                    CoroutineScope(Dispatchers.IO).launch {
                        val localCharacterPage = domainResult.toLocal()

                        try {
                            characterDao.insertListOfCharactersOrReplace(localCharacterPage.characters)
                            characterPageDao.insertPage(localCharacterPage.page)
                            Log.d("BD", "Base de datos actualizada en segundo plano")
                        }catch (e: Exception){
                            Log.d("BD", "No se ha podido insertar la página")
                        }
                    }
                }.start()
            }catch (t: Throwable){
                if (t !is RequestException){
                    throw t
                }
            }
        }
    }

    override suspend fun getCharacters(ids: List<Int>): Flow<List<CharacterEntity>> {
        return flow {
            if(ids.isEmpty()){
                emit(emptyList<CharacterEntity>())
            }else{
                characterDao.searchCharactersByIds(ids)?.let {
                    emit(it.localCharactersToDomain())
                }
                try {
                    val result = characterApi.getCharacters(ids)
                        .call()

                    val domainCharacters = result.charactersToDomain()

                    emit(domainCharacters)

                    //Update DB in background
                    Thread{
                        CoroutineScope(Dispatchers.IO).launch {
                            characterDao.insertListOfCharactersOrUpdate(
                                domainCharacters.listCharacterEntityToLocal(null)
                            )
                            Log.d("BD", "Personaje actualizado en segundo plano")
                        }
                    }.start()
                }catch (t: Throwable){
                    if (t !is RequestException){
                        throw t
                    }
                }
            }
        }
    }

    override suspend  fun getCharacter(id: Int): Flow<CharacterEntity> {
        return flow {
            characterDao.searchCharacterById(id)?.let {
                emit(it.toDomain())
            }

            try {
                val result = characterApi.getCharacter(id)
                    .call()

                emit(result.toDomain())
            }catch (t: Throwable){
                if (t !is RequestException){
                    throw t
                }
            }
        }
    }
    
}