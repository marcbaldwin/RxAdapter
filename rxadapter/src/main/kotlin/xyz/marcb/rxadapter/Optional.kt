package xyz.marcb.rxadapter

sealed class Optional<out T> {

    data class Some<out T>(val item: T): Optional<T>()
    object None: Optional<Nothing>()

    open val value: T?
        get() = when(this) {
            is Some -> item
            is None -> null
        }

}

fun <T> T?.asOptional() =
        if (this != null)
            Optional.Some(this)
        else
            Optional.None
