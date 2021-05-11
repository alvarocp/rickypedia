package es.i12capea.rickypedia.shared.data.local.dao

import es.i12capea.rickypedia.shared.data.local.model.LocalCharacterPage
import mylocal.db.LocalDb

class LocalCharacterPageDao(private val db: LocalDb){
    suspend fun getPageById(id: Int) : LocalCharacterPage? {
        return db.localCharacterPageQueries.selectLocalCharacterPage(id).executeAsOneOrNull()
    }
}