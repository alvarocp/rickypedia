package es.i12capea.rickypedia.common


data class DataState<T>(
    val error: ErrorRym? = null,
    val loading: Boolean,
    val data: T? = null
) {

    companion object {

        fun <T> error(
            code: Int,
            message: String
        ): Event<DataState<T>> {
            return Event(DataState<T>(
                loading = false,
                error = ErrorRym(code, message)
            ))
        }

        fun <T> loading(
            loading: Boolean,
            cachedData: T? = null
        ): Event<DataState<T>> {
            return Event(DataState(
                loading = loading,
                data = cachedData
            ))
        }

        fun <T> success(
            data: T
        ): Event<DataState<T>> {
            return Event(DataState(
                loading = false,
                data = data
            ))
        }
    }
}