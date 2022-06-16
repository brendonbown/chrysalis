package config

import api.ApiAccess
import prompt

// Get the configuration for API Access
class ApiConfig {

    // Get the user's API key from the 'CHRYSALIS_API_KEY' environment variable
    // or else prompt for it
    //
    // If it doesn't work, throw a config exception
    private val apiKey =
        System.getenv("CHRYSALIS_API_KEY") ?:
        prompt("API Key: ") ?:
        throw ConfigException("Unable to read API key, please try again later")

    // From the user's API key, create an API access object
    val apiAccess = ApiAccess(apiKey)
}