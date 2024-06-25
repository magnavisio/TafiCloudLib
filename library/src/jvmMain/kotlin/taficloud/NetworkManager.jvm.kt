package taficloud

import io.ktor.client.engine.apache5.*

internal actual val networkManager: NetworkManager
    get() = NetworkManager(Apache5.create())