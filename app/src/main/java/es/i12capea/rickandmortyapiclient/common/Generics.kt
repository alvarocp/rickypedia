package es.i12capea.rickandmortyapiclient.common

import es.i12capea.rickandmortyapiclient.domain.exceptions.PredicateNotSatisfiedException

inline fun <T> T.validatePredicateOrException(predicate: (T) -> Boolean): T {
    return if (predicate(this)) this else throw PredicateNotSatisfiedException()
}

inline fun <T> T.validatePredicateOrException(exception: Exception, predicate: (T) -> Boolean): T {
    return if (predicate(this)) this else throw exception
}