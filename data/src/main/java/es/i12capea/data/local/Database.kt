package es.i12capea.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import es.i12capea.data.local.converters.Converters
import es.i12capea.data.local.dao.*
import es.i12capea.data.local.model.*
import es.i12capea.rickypedia.shared.domain.common.Constants

@Database(entities = [
    LocalCharacter::class,
    LocalCharacterPage::class,
    LocalEpisode::class,
    LocalEpisodePage::class,
    LocalLocation::class,
    LocalLocationPage::class
], version = Constants.DB_VERSION)
@TypeConverters(Converters::class)
abstract class RymDatabase : RoomDatabase() {

    abstract fun getLocalCharacterDao(): LocalCharacterDao

    abstract fun getLocalCharacterPageDao(): LocalCharacterPageDao

    abstract fun getLocalEpisodeDao() : LocalEpisodeDao

    abstract fun getLocalEpisodePageDao() : LocalEpisodePageDao

    abstract fun getLocalLocationDao() : LocalLocationDao

    abstract fun getLocalLocationPageDao() : LocalLocationPageDao
}