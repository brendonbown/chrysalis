package config

import api.json.WebResource

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
            val webResAreas = apiConfig.getWebResourcePolicies(it.webResourceId)?.content ?: throw ConfigException("Unable to access API. Check your API key and " +
                    "your connection, then retry.")

            // Pair the web resource info to the list of areas
            webResAreas
        }

    return productWebResAreas
}