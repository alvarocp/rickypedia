package es.i12capea.rickypedia.shared.domain.exceptions

data class ResponseException(val code: Int, val desc: String) : Throwable() {
}