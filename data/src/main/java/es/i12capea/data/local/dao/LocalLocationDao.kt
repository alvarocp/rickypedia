package es.i12capea.data.local.dao

import androidx.room.*
import es.i12capea.data.local.model.LocalLocation

@Dao
interface LocalLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocalLocation) : Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocationList(location: List<LocalLocation>)


    @Query(
        """
        SELECT * FROM local_location 
        WHERE id IN (:ids)
    """
    )
    fun searchLocationsByIds(ids: List<Int>) : List<LocalLocation>?


    @Query(
        """
        SELECT * FROM local_location 
        WHERE id == :id
    """
    )
    fun searchLocationById(id: Int) : LocalLocation?

}