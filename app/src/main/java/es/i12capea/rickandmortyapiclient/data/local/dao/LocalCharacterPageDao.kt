package es.i12capea.rickandmortyapiclient.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.i12capea.rickandmortyapiclient.data.local.model.LocalCharacterPage

@Dao
interface LocalCharacterPageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPage(localCharacterPage: LocalCharacterPage) : Long

    @Query("""
    SELECT * FROM local_character_page
    WHERE id == :id
    """)
    fun searchPageById(id: Int) : LocalCharacterPage?

}

