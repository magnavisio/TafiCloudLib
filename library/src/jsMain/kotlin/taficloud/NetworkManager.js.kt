package taficloud

import io.ktor.client.engine.js.*

internal actual val networkManager: NetworkManager
    get() = NetworkManager(Js.create())