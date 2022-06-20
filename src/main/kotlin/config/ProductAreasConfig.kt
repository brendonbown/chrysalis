package config

import api.json.WebResource
import prompt

/**
 * Get the areas associated with each product in the list
 */
fun getProductAreas(products: List<String>): Map<WebResource, List<String>> {

    // If there aren't any products to look up, just return an empty list
    if (products.isEmpty()) return mapOf()

    // Get the API information
    val apiConfig = getApiConfig()


    val productWebResAreas = products
        .flatMap {
            // Get the web resource info
            // (if it fails, there is likely a problem with the user's key or their connection)
            val webResInfo = apiConfig.getWebResourceInfo(it)?.content ?:
                throw ConfigException("Unable to access API. Check your API key and " +
                    "your connection, then retry.")

            if (webResInfo.isEmpty()) {
                // If the query didn't match any products, let the user know
                println("The query '$it' didn't match any products")
                webResInfo
            } else if(webResInfo.size > 1) {
                throw ConfigException("Multiple products aren't yet supported")
            } else {
                // Otherwise, the query matched one item, add it to the list of products
                webResInfo
            }
        }
        .associateWith {
            // Get the policies (areas) associated with each web resource
            // (if it fails, there is likely a problem with the user's key or their connection)
            val webResAreas = apiConfig.getWebResourcePolicies(it.webResourceId)?.content ?:
                throw ConfigException("Unable to access API. Check your API key and " +
                    "your connection, then retry.")

            // Pair the web resource info to the list of areas
            webResAreas
        }

    return productWebResAreas
}

/**
 * Confirm that the user wants to perform the action with each of the areas associated with the requested web resources
 */
fun confirmProductAreas(productAreas: Map<WebResource, List<String>>, action: String): List<String> {
    val allAreas: List<String> = productAreas.map {
        var response: String?
        do {
            // Prompt for confirmation
            println("Areas for '${it.key}': ${it.value}")
            response = prompt("${capitalizeWord(action)} areas? (Y/n) ")?.lowercase() ?: run {
                // If we can't read the input, let them know, they try again (by returning null)
                println("Unable to read input. Please try again.")
                null
            }

        // Continue while the input wasn't read, the input wasn't empty, or the input wasn't 'y' or 'n'
        // (an empty input implies the default, which is 'y')
        } while (response == null || !(response.isEmpty() || response == "y" || response == "n"))

        println()

        // If the user wants to add them, return the list of areas
        if (response.isEmpty() || response == "y")
            return it.value

        // Otherwise, return an empty list (in other words, don't add any areas)
        else
            return listOf()
    }.flatten()

    return allAreas
}

fun capitalizeWord(word: String) = word.replaceFirstChar(Char::uppercaseChar)