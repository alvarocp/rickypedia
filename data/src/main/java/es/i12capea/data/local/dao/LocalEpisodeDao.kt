package es.i12capea.data.local.dao

import androidx.room.*
import es.i12capea.data.local.model.LocalEpisode

@Dao
interface LocalEpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(episode: LocalEpisode) : Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListEpisode(episodes: List<LocalEpisode>)

    @Query(
        """
        SELECT * FROM local_episode 
        WHERE id IN (:ids)
    """
    )
    fun searchEpisodesByIds(ids: List<Int>) : List<LocalEpisode>?


    @Query(
        """
        SELECT * FROM local_episode 
        WHERE id == :id
    """
    )
    fun searchEpisodeById(id: Int) : LocalEpisode?


}