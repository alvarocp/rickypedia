package es.i12capea.rickandmortyapiclient.data.local.dao

import androidx.room.*
import es.i12capea.rickandmortyapiclient.data.local.model.LocalLocationPage
import es.i12capea.rickandmortyapiclient.data.local.model.PageAndLocations

@Dao
interface LocalLocationPageDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPage(localLocationpage: LocalLocationPage) : Long

    @Transaction
    @Query(
        """
    SELECT * FROM local_location_page
    WHERE actualPage == :id
    """
    )
    fun searchPageById(id: Int) : PageAndLocations?

}

