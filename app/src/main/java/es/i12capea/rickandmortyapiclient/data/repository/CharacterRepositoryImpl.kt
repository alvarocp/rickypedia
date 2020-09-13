package es.i12capea.rickandmortyapiclient.data.repository

import es.i12capea.rickandmortyapiclient.data.api.CharacterApi
import es.i12capea.rickandmortyapiclient.data.api.call
import es.i12capea.rickandmortyapiclient.data.mappers.toDomain
import es.i12capea.rickandmortyapiclient.domain.entities.CharacterEntity
import es.i12capea.rickandmortyapiclient.domain.repositories.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepositoryImpl @Inject constructor(
    private val characterApi: CharacterApi
) : CharacterRepository{

    override suspend fun getAllCharacters(page: Int?): Flow<List<CharacterEntity>> {
        return flow{

            val result = characterApi.getAllCharacters(page)
                .call()

            val list = ArrayList<CharacterEntity>()

            for(c in result.results){
                list.add(c.toDomain())
            }

            emit(list)
        }
    }

    override suspend fun getCharacters(ids: List<Int>): Flow<List<CharacterEntity>> {
        return flow {
            val result = characterApi.getCharacters(ids)
                .call()

            val list = ArrayList<CharacterEntity>()

            for (c in result.results) {
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