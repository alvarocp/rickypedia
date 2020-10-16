package es.i12capea.rickandmortyapiclient.data.repository

import android.graphics.pdf.PdfDocument
import es.i12capea.rickandmortyapiclient.data.NetworkBoundResource
import es.i12capea.rickandmortyapiclient.data.api.CharacterApi
import es.i12capea.rickandmortyapiclient.data.api.call
import es.i12capea.rickandmortyapiclient.data.api.models.PageableResponse
import es.i12capea.rickandmortyapiclient.data.api.models.character.RemoteCharacter
import es.i12capea.rickandmortyapiclient.data.local.dao.LocalCharacterPageDao
import es.i12capea.rickandmortyapiclient.data.local.model.LocalCharacterPage
import es.i12capea.rickandmortyapiclient.data.mappers.characterPageToDomain
import es.i12capea.rickandmortyapiclient.data.mappers.getActualPage
import es.i12capea.rickandmortyapiclient.data.mappers.toDomain
import es.i12capea.rickandmortyapiclient.domain.entities.CharacterEntity
import es.i12capea.rickandmortyapiclient.domain.entities.PageEntity
import es.i12capea.rickandmortyapiclient.domain.exceptions.RequestException
import es.i12capea.rickandmortyapiclient.domain.repositories.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepositoryImpl @Inject constructor(
    private val characterApi: CharacterApi,
    private val characterPageDao: LocalCharacterPageDao
) : CharacterRepository{

    override suspend fun getCharactersAtPage(page: Int): Flow<PageEntity<CharacterEntity>> {
        return flow{
            characterPageDao.searchPageById(page)?.let {
                emit(it.toDomain())
            }
            try {
                val result = characterApi.getAllCharacters(page)
                    .call()

                val localCharacterPage = LocalCharacterPage(
                    id = result.info.getActualPage(),
                    info = result.info,
                    results = result.results
                )
                val insertResult = characterPageDao.insertPage(localCharacterPage)
                if(insertResult.toInt() == localCharacterPage.id){
                    emit(result.characterPageToDomain())
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
            val result = characterApi.getCharacters(ids)
                .call()

            val list = ArrayList<CharacterEntity>()

            for (c in result) {
                list.add(c.toDomain())
            }

            emit(list)
        }
    }

    override suspend  fun getCharacter(id: Int): Flow<CharacterEntity> {
        return flow {

            val result = characterApi.getCharacter(id)
                .call()

            emit(result.toDomain())
        }
    }
}