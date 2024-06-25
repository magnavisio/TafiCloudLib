package taficloud.errors

class CloudLoomError(override val message: String, override val cause: Throwable? = null) : Exception(message, cause)

internal fun loomError(message: String, error: Exception? = null): Nothing = throw CloudLoomError(message, error)
internal expect fun errorHandler(error: Exception):Nothing