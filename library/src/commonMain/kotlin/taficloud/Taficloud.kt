package taficloud

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import taficloud.errors.errorHandler
import taficloud.models.ApiResponse
import taficloud.models.MediaFile

class Taficloud(private val apiKey: String) {

    private val client = networkManager.client

    /**
     * @param fileName the file name wirth the extension
     */
    suspend fun upload(file: ByteArray, fileName: String, folder: String): MediaFile {
        try {
            val response = client.submitFormWithBinaryData(url = "/media/upload", formData = formData {
                append("folder", folder)
                append("file", file, Headers.build {
                    append(HttpHeaders.ContentType, ContentType.defaultForFileExtension(fileName.split(".").last()))
                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                })
            }) {
                appendAuth()
            }.body<ApiResponse<MediaFile>>()
            return response.data
        } catch (e: Exception) {
            errorHandler(e)
        }
    }

    private fun HttpRequestBuilder.appendAuth() {
        headers {
            append(HttpHeaders.Authorization, "Bearer $apiKey")
        }
    }

    suspend fun uploadBase64() {

    }

    suspend fun uploadMultiple(vararg files: String) {
    }


    suspend fun downloadFile(media: String) {

    }

    suspend fun convertMedia(mediaLey: String, format: String, folder: String) {

    }

    suspend fun compressPng(
        compressionLevel: Int,
        adaptiveFiltering: Boolean,
        palette: Boolean,
        quality: Int,
        effort: Int,
    ) {

    }

    suspend fun compressJpg(
        quality: Int,
        progressive: Boolean,
        chromaSubsampling: String,
        trellisQuantisation: Boolean,
        overshootDeringing: Boolean,
        optimizeScans: Boolean,
        mozJpeg: Boolean,
    ) {

    }

    suspend fun compressWebp(
        quality: Int,
        alphaQuality: Int,
        lossless: Boolean,
        nearLossless: Boolean,
        smartSubSample: Boolean,
        effort: Int,
    ) {

    }

    suspend fun compressTiff(
        quality: Int,
        compression: String,
        predictor: String,
        tile: Boolean,
        tileWidth: Boolean,
        tileHeight: Boolean,
        pyramid: Boolean,
        bitDepth: Int,
    ) {

    }

    suspend fun compressGif(
        reoptimise: Boolean,
        effort: Int,
    ) {

    }

    suspend fun compressAvif(
        quality: Int,
        lossless: String,
        speed: Int,
        chromaSubsampling: String,
    ) {
    }

    suspend fun getMedia(id: Int) {

    }

    suspend fun deleteMedia(id: Int) {

    }

    suspend fun bulkDelete(files: Map<Int, String>) {

    }


}