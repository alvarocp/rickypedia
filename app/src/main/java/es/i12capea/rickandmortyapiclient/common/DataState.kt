package es.i12capea.rickandmortyapiclient.common


data class DataState<T>(
    val error: ErrorRym? = null,
    val loading: Boolean,
    val data: T? = null
) {

    companion object {

        fun <T> error(
            code: Int,
            message: String
        ): DataState<T> {
            return DataState(
                loading = false,
                error = ErrorRym(code, message)
            )
        }

        fun <T> loading(
            loading: Boolean,
            cachedData: T? = null
        ): DataState<T> {
            return DataState(
                loading = loading,
                data = cachedData
            )
        }

        fun <T> success(
            data: T
        ): DataState<T> {
            return DataState(
                loading = false,
                data = data
            )
        }
    }
}