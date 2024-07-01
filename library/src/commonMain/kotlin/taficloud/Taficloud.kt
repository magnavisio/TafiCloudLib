package taficloud

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import taficloud.errors.errorHandler
import taficloud.models.ApiResponse
import taficloud.models.Media
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
     * Upload a file in base64 format
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
                }.encodeToString())
            }.body<ApiResponse<MediaFile>>()
            response.data
        }
    }

    /**
     * Upload multiple files
     * @param files is a map of the file ByteArray to the file name(with extension)
     */
    suspend fun uploadMultiple(files: Map<ByteArray, String>): List<MediaFile> {
        return handleRequest {
            client.submitFormWithBinaryData(url = "media/upload/multiple", formData = formData {
                files.forEach {
                    val fileName = it.value
                    append("files", it.key, Headers.build {
                        append(HttpHeaders.ContentType, ContentType.defaultForFileExtension(fileName.split(".").last()))
                        append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                    })
                }
            }) {
                appendAuth()
            }.body<ApiResponse<Media>>().data.media
        }
    }

    /**
     * Download a file by the key
     */
    suspend fun downloadFile(key: String): ByteArray {
        return handleRequest {
            client.get("media/download") {
                appendAuth()
                url {
                    parameters.append("media", key)
                    parameters.append("download", "true")
                }
            }.readBytes()
        }
    }

    /**
     * Compress a png file
     * @param compressionLevel: 0 (no compression) to 9 (maximum compression).
     * @param adaptiveFiltering: Use adaptive filtering (true/false).
     * @param palette: Use palette-based color reduction (true/false).
     * @param quality: 0 to 100 (if palette is true).
     * @param effort: 0 (fastest) to 10 (slowest).
     */
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
                append("options", options.encodeToString())
                appendFile(file, fileName)
            }) {
            appendAuth()
        }.readBytes()

    /**
     * Convert **.jpg** and **.jpeg** images
     * @param quality: 0 to 100.
     * @param progressive: Create a progressive JPEG (true/false).
     * @param chromaSubsampling: '4:4:4', '4:2:2', or '4:2:0'.
     * @param trellisQuantisation: Use trellis quantization (true/false).
     * @param overshootDeringing: Use overshoot deringing (true/false).
     * @param optimizeScans: Optimize scans for progressive JPEG (true/false).
     * @param mozjpeg: Use MozJPEG for compression (true/false).
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

    /**
     * @param quality: 0 to 100.
     *
     * @param alphaQuality: 0 to 100.
     *
     * @param lossless: Use lossless compression (true/false).
     *
     * @param nearLossless: Use near-lossless compression (true/false).
     *
     * @param smartSubSample: Use smart subsampling (true/false).
     *
     * @param effort: 0 (fastest) to 10 (slowest).
     */
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


    /**
     * @param quality: 0 to 100.
     * @param compression: 'lzw', 'deflate', or 'jpeg'.
     * @param predictor: 'none', 'horizontal', or 'float'.
     * @param tile: Use tiling (true/false).
     * @param tileWidth: Tile width (if tile is true).
     * @param tileHeight: Tile height (if tile is true).
     * @param pyramid: Create a multi-resolution pyramid image (true/false).
     * @param bitdepth: 8 or 16.
     */
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

    /**
     * @param reoptimise: Re-optimize the GIF (true/false).
     * @param effort: 0 (fastest) to 10 (slowest).
     */
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

    /**
     * @param quality: 0 to 100.
     * @param lossless: Use lossless compression (true/false).
     * @param speed: 0 (slowest) to 10 (fastest).
     * @param chromaSubsampling: '4:4:4', '4:2:2', or '4:2:0'.
     */
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
}