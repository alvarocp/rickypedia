package es.i12capea.data.local.dao

import androidx.room.*
import es.i12capea.data.local.model.LocalCharacterPage
import es.i12capea.data.local.model.PageAndCharacters

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

