package es.i12capea.rickypedia.shared.domain.common


object Constants {
    const val BASE_URL = "https://rickandmortyapi.com/api/"
    const val DB_VERSION = 1
    const val DB_NAME = "rym_db"
    const val MAX_ITEM_PER_PAGE = 20

    const val NO_ERROR = 0
    const val NO_NETWORK = 1
    const val BAD_NETWORK_RESPONSE = 2
    const val PREDICATE_NOT_SATISFIED = 3
    const val UNKNOWN_ERROR = 9999
}