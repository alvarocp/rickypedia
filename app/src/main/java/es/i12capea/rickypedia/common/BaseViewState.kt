package es.i12capea.rickypedia.common

import es.i12capea.domain.common.Constants

open class BaseViewState(
    var isLoading: Boolean = false,
    var errorRym: Event<ErrorRym> = Event(ErrorRym(Constants.NO_ERROR, ""))
)