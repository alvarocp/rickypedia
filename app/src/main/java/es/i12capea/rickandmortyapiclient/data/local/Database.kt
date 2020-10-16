package es.i12capea.rickandmortyapiclient.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import es.i12capea.rickandmortyapiclient.common.Constants
import es.i12capea.rickandmortyapiclient.data.api.models.character.RemoteCharacter
import es.i12capea.rickandmortyapiclient.data.local.converters.Converters
import es.i12capea.rickandmortyapiclient.data.local.dao.LocalCharacterPageDao
import es.i12capea.rickandmortyapiclient.data.local.dao.RemoteCharacterDao
import es.i12capea.rickandmortyapiclient.data.local.model.LocalCharacterPage


@Database(entities = [RemoteCharacter::class, LocalCharacterPage::class], version = Constants.DB_VERSION)
@TypeConverters(Converters::class)
abstract class RymDatabase : RoomDatabase() {

    abstract fun getRemoteCharacterDao(): RemoteCharacterDao

    abstract fun getLocalCharacterPageDao(): LocalCharacterPageDao
}