package es.i12capea.rickandmortyapiclient.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.i12capea.rickandmortyapiclient.data.api.models.character.RemoteCharacter

@Dao
interface RemoteCharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: RemoteCharacter) : Long

    @Query("""
        SELECT * FROM remote_character 
        WHERE id == :id
    """)
    fun searchCharacterById(id: Int) : RemoteCharacter

}