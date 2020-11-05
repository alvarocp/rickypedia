package es.i12capea.rickypedia.data.local.dao

import androidx.room.*
import es.i12capea.rickypedia.data.local.converters.Converters
import es.i12capea.rickypedia.data.local.model.LocalCharacter

@Dao
interface LocalCharacterDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCharacter(character: LocalCharacter) : Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfCharactersOrReplace(characters: List<LocalCharacter>)

    suspend fun insertOrUpdate(character: LocalCharacter){
        try {
            insertCharacter(character)
        }catch (t: Throwable){
            updateCharacter(character.id,  Converters().fromListInt(character.episodes) )
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


    @Query(
        """
        SELECT * FROM local_character 
        WHERE id == :id
    """
    )
    fun searchCharacterById(id: Int) : LocalCharacter?

    @Query(
        """
        SELECT * FROM local_character 
        WHERE id IN (:ids)
    """
    )
    fun searchCharactersByIds(ids: List<Int>) : List<LocalCharacter>?

}