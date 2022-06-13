package api.json

import kotlinx.serialization.json.Json

val decoder = Json { ignoreUnknownKeys = true }