package es.i12capea.rickypedia.shared.data.local.dao

import es.i12capea.rickypedia.shared.data.local.listOfIntsAdapter
import es.i12capea.rickypedia.shared.data.local.localLocationShortAdapter
import es.i12capea.rickypedia.shared.data.local.model.LocalCharacter
import mylocal.db.LocalDb

class LocalCharacterDao(private val db : LocalDb) {


    val adapter = LocalCharacter.Adapter(
        localLocationShortAdapter,
        localLocationShortAdapter,
        listOfIntsAdapter
    )

    suspend fun getCharactersAtPage(page: Int): List<LocalCharacter> {
        return db.localCharacterQueries.selectCharactersInPage(page).executeAsList()
    }

    suspend fun insertCharacterOrAbort(character: LocalCharacter) {
        db.localCharacterQueries.insertCharacter(
            character.id,
            character.page,
            character.name,
            character.status,
            character.species,
            character.type,
            character.gender,
            character.origin,
            character.location,
            character.image,
            character.episodes
        )
    }

    suspend fun insertListOfCharactersOrReplace(characters: List<LocalCharacter>){
        db.transaction {
            for (character in characters){
                db.localCharacterQueries.insertCharacter(
                    character.id,
                    character.page,
                    character.name,
                    character.status,
                    character.species,
                    character.type,
                    character.gender,
                    character.origin,
                    character.location,
                    character.image,
                    character.episodes
                )
            }
        }
    }
}