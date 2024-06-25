package taficloud.models

import kotlinx.serialization.Serializable

@Serializable
internal data class ApiResponse<T> (
    val statusCode: Long,
    val message: String,
    val data: T
)
