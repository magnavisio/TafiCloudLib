package com.magnavisio.taficloud

import taficloud.Taficloud
import java.io.File

suspend fun main() {
    val taficloud = Taficloud(System.getenv("apiKey"))
    println("Enter func")
    do {
        val func = readlnOrNull()
        when (func) {
            "upload" -> {
                val file = File("src/main/resources/coolio.png")
                val response = taficloud.upload(file.readBytes(), fileName = file.name, "samples")
                println(response)
            }

            "uploadBase64" -> {
                val file = File("src/main/resources/coolio.png")
                val response = taficloud.uploadBase64(System.getenv("fileBase64"),"samples")
                println(response)
            }

            "uploadMultiple" -> {

                val response = taficloud.uploadMultiple(
                    mapOf(
                        File("src/main/resources/coolio.png").readBytes() to "coolio.png",
                        File("src/main/resources/scrnshot.png").readBytes() to "scnshot.png"
                    )
                )
                println(response)
            }

            "download" -> {

                val response = taficloud.downloadFile("dewallet/samples/coolio-3288618605bb3b1c918a1d01531926110.png")
                val dir = File("src/main/resources/downloads")
                dir.mkdirs()
                val output = File(dir, "/download.png")
                output.createNewFile()
                output.writeBytes(response)
                println(response)
            }

            "compressPng" -> {
                val file = File("src/main/resources/coolio.png")
                val response = taficloud.compressPng(
                    file = file.readBytes(),
                    fileName = file.name,
                    compressionLevel = 5,
                    adaptiveFiltering = false,
                    palette = false,
                    quality = 50,
                    effort = 5
                )
                val dir = File("src/main/resources/downloads")
                dir.mkdirs()
                val output = File(dir, "/coolio_compressed.png")
                output.createNewFile()
                output.writeBytes(response)
                println(response)
            }

            "help" -> {
                println("Taficloud lib jvm sample")
                println("Use the following functions to test different functions:")
                println()
                println("upload - > Upload a file")
                println("uploadBase64 - > Upload a file as bas64 string")
            }

            else -> {
                println("Unknown command")
            }
        }
        println("Func complete")
    } while (func != null)
}

//fun main() {
//
//}