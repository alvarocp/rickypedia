package es.i12capea.rickypedia.domain.exceptions

data class ResponseException(val code: Int, val desc: String) : Throwable() {
}