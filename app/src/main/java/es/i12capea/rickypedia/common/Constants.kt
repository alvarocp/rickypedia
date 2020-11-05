package es.i12capea.rickypedia.common

import es.i12capea.rickypedia.domain.exceptions.ResponseException


object Constants {
    val BASE_URL = "https://rickandmortyapi.com/api/"


    const val DB_VERSION = 1
    const val DB_NAME = "rym_db"
    const val MAX_ITEM_PER_PAGE = 20

    val THROWABLE_ERRORS : HashMap<String, ErrorRym> = hashMapOf(
        ResponseException::class.java.name to ErrorRym(1, "")
    )
}