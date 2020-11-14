package es.i12capea.rickypedia.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import es.i12capea.rickypedia.common.Constants
import es.i12capea.rickypedia.data.local.converters.Converters
import es.i12capea.rickypedia.data.local.dao.*
import es.i12capea.rickypedia.data.local.model.*


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