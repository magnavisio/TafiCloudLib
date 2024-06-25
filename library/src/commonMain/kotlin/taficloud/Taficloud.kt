package taficloud

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import taficloud.errors.errorHandler
import taficloud.models.ApiResponse
import taficloud.models.MediaFile

class Taficloud(private val apiKey: String) {

    private val client = networkManager.client

    /**
     * @param fileName the file name wirth the extension
     */
    suspend fun upload(file: ByteArray, fileName: String, folder: String): MediaFile {
        return handleRequest {
            val response = client.submitFormWithBinaryData(url = "/media/upload", formData = formData {
                append("folder", folder)
                appendFile(file, fileName)
            }) {
                appendAuth()
            }.body<ApiResponse<MediaFile>>()
            response.data
        }
    }

    private fun FormBuilder.appendFile(file: ByteArray, fileName: String) {
        append("file", file, Headers.build {
            append(HttpHeaders.ContentType, ContentType.defaultForFileExtension(fileName.split(".").last()))
            append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
        })
    }

    private fun HttpRequestBuilder.appendAuth() {
        headers {
            append(HttpHeaders.Authorization, "Bearer $apiKey")
        }
    }

    private suspend fun <T> handleRequest(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: Exception) {
            errorHandler(e)
        }
    }

    /**
     * @param file the file in base64 format string
     * @param folder the folder name the file should be uploaded to
     */
    suspend fun uploadBase64(file: String, folder: String): MediaFile {
        return handleRequest {
            val response = client.post(urlString = "/media/upload/base64") {
                appendAuth()
                setBody(buildJsonObject {
                    put("file", file)
                    put("folder", folder)
                })
            }.body<ApiResponse<MediaFile>>()
            response.data
        }
    }

    /**
     * @param files is a map of the file ByteArray to the file name(with extension)
     */
    suspend fun uploadMultiple(files: Map<ByteArray, String>) {
        return handleRequest {
            client.submitFormWithBinaryData(url = "/media/upload", formData = formData {
                files.forEach {
                    val fileName = it.value
                    append("files", it.key, Headers.build {
                        append(HttpHeaders.ContentType, ContentType.defaultForFileExtension(fileName.split(".").last()))
                        append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                    })
                }
            }) {
                appendAuth()
            }.body<ApiResponse<MediaFile>>()
        }
    }


    suspend fun downloadFile(key: String): ByteArray {
        return handleRequest {
            client.get("media/download") {
                appendAuth()
                request {
                    parameter("media", key)
                    parameter("download", true)
                }
            }.readBytes()
        }
    }

    suspend fun compressPng(
        file: ByteArray,
        fileName: String,
        compressionLevel: Int,
        adaptiveFiltering: Boolean,
        palette: Boolean,
        quality: Int,
        effort: Int,
    ): ByteArray {
        return handleRequest {
            compressFile(
                file = file,
                fileName = fileName,
                options = buildJsonObject {
                    put("compressionLevel", compressionLevel)
                    put("adaptiveFiltering", adaptiveFiltering)
                    put("palette", palette)
                    put("quality", quality)
                    put("effort", effort)
                }
            )
        }
    }

    private suspend fun compressFile(file: ByteArray, fileName: String, options: JsonObject) =
        client.submitFormWithBinaryData(
            url = "/media/compress-img-file",
            formData = formData {
                append("options", options.let {
                    Json.encodeToString(it)
                })
                appendFile(file, fileName)
            }) {
            appendAuth()
        }.readBytes()

    /**
     * Convert **.jpg** and **.jpeg** images
     */
    suspend fun compressJpg(
        file: ByteArray,
        fileName: String,
        quality: Int,
        progressive: Boolean,
        chromaSubsampling: String,
        trellisQuantisation: Boolean,
        overshootDeringing: Boolean,
        optimizeScans: Boolean,
        mozJpeg: Boolean,
    ): ByteArray {
        return handleRequest {
            compressFile(
                file = file,
                fileName = fileName,
                options = buildJsonObject {
                    put("quality", quality)
                    put("progressive", progressive)
                    put("chromaSubsampling", chromaSubsampling)
                    put("trellisQuantisation", trellisQuantisation)
                    put("overshootDeringing", overshootDeringing)
                    put("optimizeScans", optimizeScans)
                    put("mozjpeg", mozJpeg)
                }
            )
        }
    }

    suspend fun compressWebp(
        file: ByteArray,
        fileName: String,
        quality: Int,
        alphaQuality: Int,
        lossless: Boolean,
        nearLossless: Boolean,
        smartSubSample: Boolean,
        effort: Int,
    ): ByteArray {
        return handleRequest {
            compressFile(
                file = file,
                fileName = fileName,
                options = buildJsonObject {
                    put("quality", quality)
                    put("alphaQuality", alphaQuality)
                    put("lossless", lossless)
                    put("nearLossless", nearLossless)
                    put("smartSubsample", smartSubSample)
                    put("effort", effort)
                }
            )
        }
    }

    suspend fun compressTiff(
        file: ByteArray,
        fileName: String,
        quality: Int,
        compression: String,
        predictor: String,
        tile: Boolean,
        tileWidth: Boolean,
        tileHeight: Boolean,
        pyramid: Boolean,
        bitDepth: Int,
    ): ByteArray {
        return handleRequest {
            compressFile(
                file = file,
                fileName = fileName,
                options = buildJsonObject {
                    put("quality", quality)
                    put("compression", compression)
                    put("predictor", predictor)
                    put("tile", tile)
                    put("tileWidth", tileWidth)
                    put("tileHeight", tileHeight)
                    put("pyramid", pyramid)
                    put("bitdepth", bitDepth)
                }
            )
        }
    }

    suspend fun compressGif(
        file: ByteArray,
        fileName: String,
        reoptimise: Boolean,
        effort: Int,
    ): ByteArray {
        return handleRequest {
            compressFile(
                file = file,
                fileName = fileName,
                options = buildJsonObject {
                    put("reoptimise", reoptimise)
                    put("effort", effort)
                }
            )
        }
    }

    suspend fun compressAvif(
        file: ByteArray,
        fileName: String,
        quality: Int,
        lossless: String,
        speed: Int,
        chromaSubsampling: String,
    ) {
        return handleRequest {
            compressFile(
                file = file,
                fileName = fileName,
                options = buildJsonObject {
                    put("quality", quality)
                    put("lossless", lossless)
                    put("speed", speed)
                    put("chromaSubsampling", chromaSubsampling)
                }
            )
        }
    }

    suspend fun deleteMedia(id: Int): String {
        return handleRequest {
            client.delete("/media/$id") {
                appendAuth()
            }.body<ApiResponse<String>>().data
        }
    }

    /**
     * Delete multiple files
     * @param files lost of file Id's
     */
    suspend fun bulkDelete(files: List<Int>) {
        return handleRequest {
            client.delete("/media/bulk/delete") {
                appendAuth()
                setBody(buildJsonObject {
                    put("files", buildJsonArray {
                        files.forEach { fileId ->
                            add(buildJsonObject {
                                put("type", "file")
                                put("id", fileId)
                            })
                        }
                    })
                })
            }.body<ApiResponse<JsonObject>>().message
        }
    }


}