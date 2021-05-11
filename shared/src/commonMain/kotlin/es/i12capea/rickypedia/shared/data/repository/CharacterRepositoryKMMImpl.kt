package es.i12capea.rickypedia.shared.data.repository

import es.i12capea.rickypedia.shared.data.api.CharacterApi
import es.i12capea.rickypedia.shared.data.local.dao.LocalCharacterDao
import es.i12capea.rickypedia.shared.data.local.dao.LocalCharacterPageDao
import es.i12capea.rickypedia.shared.data.mappers.toDomainCharacters
import es.i12capea.rickypedia.shared.domain.entities.CharacterEntity
import es.i12capea.rickypedia.shared.domain.entities.PageEntity
import es.i12capea.rickypedia.shared.domain.repositories.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CharacterRepositoryKMMImpl(
    private val characterDao: LocalCharacterDao,
    private val characterPageDao: LocalCharacterPageDao,
    private val characterApi: CharacterApi
) : CharacterRepository {
    override suspend fun getCharactersAtPage(page: Int): Flow<PageEntity<CharacterEntity>> {
        return flow {

            characterPageDao.getPageById(page)?.let { localPage ->
                characterDao.getCharactersAtPage(page).let { localCharacters ->
                    if (localCharacters.size == localPage.count){
                        emit(PageEntity(
                            localPage.nextPage,
                            localPage.prevPage,
                            localPage.actualPage,
                            localPage.count,
                            localCharacters.toDomainCharacters()
                        ))
                    }
                }
            }

            characterApi.getCharactersAtPage(page).apply {

            }
        }
    }

    override suspend fun getCharacters(ids: List<Int>): Flow<List<CharacterEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCharacter(id: Int): Flow<CharacterEntity> {
        TODO("Not yet implemented")
    }
}