package es.i12capea.data.local.dao

import androidx.room.*
import es.i12capea.data.local.converters.Converters
import es.i12capea.data.local.model.LocalCharacter

@Dao
interface LocalCharacterDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCharacterOrAbort(character: LocalCharacter) : Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfCharactersOrReplace(characters: List<LocalCharacter>)

    suspend fun insertOrUpdate(character: LocalCharacter){
        try {
            insertCharacterOrAbort(character)
        }catch (t: Throwable){
            character.pageId?.let {
                updateCharacterPageAndEpisodes(
                    character.id,
                    it,
                    Converters().fromListInt(character.episodes)
                )
            } ?: updateCharacter(
                character.id,
                Converters().fromListInt(character.episodes)
            )
        }
    }

    @Transaction
    suspend fun insertListOfCharactersOrUpdate(characters: List<LocalCharacter>){
        for (character in characters){
            insertOrUpdate(character)
        }
    }

    @Query("""
        UPDATE local_character
        SET episodes = :episodes
        WHERE id = :id
    """)
    fun updateCharacter(id: Int, episodes: String)

    @Query("""
        UPDATE local_character
        SET pageId = :pageId,
        episodes = :episodes
        WHERE id = :id
    """)
    fun updateCharacterPageAndEpisodes(id: Int, pageId: Int, episodes: String)

    @Query("""
        SELECT * FROM local_character 
        WHERE id == :id
        """)
    fun searchCharacterById(id: Int) : LocalCharacter?

    @Query("""
        SELECT * FROM local_character 
        WHERE id IN (:ids)
    """)
    fun searchCharactersByIds(ids: List<Int>) : List<LocalCharacter>?

}