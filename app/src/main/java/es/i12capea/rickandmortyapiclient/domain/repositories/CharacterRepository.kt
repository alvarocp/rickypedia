package es.i12capea.rickandmortyapiclient.domain.repositories

import es.i12capea.rickandmortyapiclient.domain.entities.CharacterEntity
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun getAllCharacters(page: Int? = null) : Flow<List<CharacterEntity>>
    suspend fun getCharacters(ids: List<Int>) : Flow<List<CharacterEntity>>
    suspend fun getCharacter(id: Int) : Flow<CharacterEntity>
}