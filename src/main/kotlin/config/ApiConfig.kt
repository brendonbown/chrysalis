package config

import api.ApiAccess
import getDebugEnv
import prompt

fun getApiConfig(): ApiAccess {
    // Get the user's API key from the 'CHRYSALIS_API_KEY' environment variable
    // or else prompt for it
    //
    // If it doesn't work, throw a config exception
    val apiKey =
        getDebugEnv("CHRYSALIS_API_KEY") ?:
        prompt("API Key: ") ?:
        throw ConfigException("Unable to read API key, please try again later")

    println()

    // From the user's API key, create an API access object
    return ApiAccess(apiKey)
}