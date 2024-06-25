package taficloud

import io.ktor.client.engine.cio.*


internal actual val networkManager: NetworkManager
    get() = NetworkManager(CIO.create {})