package es.i12capea.rickandmortyapiclient.data.local.dao

import androidx.room.*
import es.i12capea.rickandmortyapiclient.data.local.model.LocalCharacter
import es.i12capea.rickandmortyapiclient.data.local.model.LocalCharacterPage
import es.i12capea.rickandmortyapiclient.data.local.model.PageAndCharacters

@Dao
interface LocalCharacterPageDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPage(localCharacterPage: LocalCharacterPage) : Long

    @Transaction
    @Query(
        """
    SELECT * FROM local_character_page
    WHERE actualPage == :id
    """
    )
    fun searchPageById(id: Int) : PageAndCharacters?

}

