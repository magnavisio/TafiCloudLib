package taficloud.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaFile (
    val id: Long,
    @SerialName("organizationId")
    val organizationID: Long,
    val name: String,
    val url: String,
    val key: String,
    val bucket: String,
    val mimetype: String,
    val size: Double,
    val metaData: MetaData,
    @SerialName("folderId")
    val folderID: Long,
    val updatedAt: String,
    val createdAt: String
)

@Serializable
data class MetaData (
    val ipAddress: String,
    val userAgent: String
)