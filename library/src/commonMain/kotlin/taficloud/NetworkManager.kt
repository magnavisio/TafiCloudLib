package taficloud

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json


internal expect val networkManager: NetworkManager

internal class NetworkManager(engine: HttpClientEngine) {
    val client: HttpClient = HttpClient(engine) {
        defaultRequest {
            url("https://cloudloom-api-dev.craftme.dev")
        }
        install(Logging) {
            level = LogLevel.ALL
        }
        install(HttpTimeout) {
            socketTimeoutMillis = 2000
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }
}