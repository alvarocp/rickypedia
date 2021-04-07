package es.i12capea.data.local.dao

import androidx.room.*
import es.i12capea.data.local.model.LocalEpisodePage
import es.i12capea.data.local.model.PageAndEpisodes

@Dao
interface LocalEpisodePageDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPage(localEpisodePage: LocalEpisodePage) : Long



    @Transaction
    @Query(
        """
    SELECT * FROM local_episode_page
    WHERE actualPage == :id
    """
    )
    fun searchPageById(id: Int) : PageAndEpisodes?

}

