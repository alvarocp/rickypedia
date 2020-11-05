package es.i12capea.rickypedia.common

import es.i12capea.rickypedia.domain.exceptions.PredicateNotSatisfiedException

inline fun <T> T.validatePredicateOrException(predicate: (T) -> Boolean): T {
    return if (predicate(this)) this else throw PredicateNotSatisfiedException()
}

inline fun <T> T.validatePredicateOrException(exception: Exception, predicate: (T) -> Boolean): T {
    return if (predicate(this)) this else throw exception
}