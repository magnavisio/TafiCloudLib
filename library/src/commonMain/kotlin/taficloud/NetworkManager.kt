package taficloud

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*


internal expect val networkManager: NetworkManager

internal class NetworkManager(engine: HttpClientEngine) {
    val client: HttpClient = HttpClient(engine) {
        defaultRequest{
            url("https://cloudloom-api-dev.craftme.dev")
        }
        install(Logging) {
            level = LogLevel.BODY
        }
        install(HttpTimeout){
            socketTimeoutMillis = 2000
        }
        install(ContentNegotiation) {
            json()
        }
    }
}