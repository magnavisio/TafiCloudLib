package taficloud.errors


internal actual fun errorHandler(error: Exception): Nothing {
    throw error
}