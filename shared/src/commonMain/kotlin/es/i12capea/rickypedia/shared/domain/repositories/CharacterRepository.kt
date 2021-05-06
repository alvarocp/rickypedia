package es.i12capea.rickypedia.shared.domain.repositories

import es.i12capea.rickypedia.shared.domain.entities.CharacterEntity
import es.i12capea.rickypedia.shared.domain.entities.PageEntity
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun getCharactersAtPage(page: Int) : Flow<PageEntity<CharacterEntity>>
    suspend fun getCharacters(ids: List<Int>) : Flow<List<CharacterEntity>>
    suspend fun getCharacter(id: Int) : Flow<CharacterEntity>
}