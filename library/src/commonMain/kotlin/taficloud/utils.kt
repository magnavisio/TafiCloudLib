package taficloud

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

internal fun JsonElement.encodeToString(): String {
    return Json.encodeToString(this)
}