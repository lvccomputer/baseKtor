package android.ncdev.common.coroutines

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
sealed class Resource<out T> {
    data class Success<T>(val data: T?) : Resource<T>()
    data class Error(val error:Throwable) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
    object OnLoadingFinished : Resource<Nothing>()

    companion object {
        fun <T>fromResult(result: Result<T>): Resource<T> {
            return Success(
                result.getOrElse {
                    return Error(it)
                }
            )
        }
    }

    fun use(
        onSuccess: ((T) -> Unit)? = null,
        onFailed: ((Error) -> Unit)? = null,
        onLoading: (() -> Unit)? = null,
        onLoadingFinished: (() -> Unit)? = null
    ) {
        when (this) {
            is Success -> {
                onLoadingFinished?.invoke()
                if (data != null) {
                    onSuccess?.invoke(data)
                }
            }
            is Error -> {
                onLoadingFinished?.invoke()
                onFailed?.invoke(this)
            }
            is Loading -> {
                onLoading?.invoke()
            }
            is OnLoadingFinished -> {
                onLoadingFinished?.invoke()
            }
        }
    }
}
