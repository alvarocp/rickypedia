package es.i12capea.domain.exceptions

data class ResponseException(val code: Int, val desc: String) : Throwable() {
}