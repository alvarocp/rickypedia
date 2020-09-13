package es.i12capea.rickandmortyapiclient.common

import es.i12capea.rickandmortyapiclient.domain.exceptions.ResponseException


object Constants {
    val BASE_URL = "https://rickandmortyapi.com/api/"

    val THROWABLE_ERRORS : HashMap<String, ErrorRym> = hashMapOf(
        ResponseException::class.java.name to ErrorRym(1, "")
    )
}