package api

import api.json.ResourcePolicyResponse
import api.json.WebResourceResponse
import api.json.decoder
import okhttp3.OkHttpClient
import okhttp3.Request

class ApiAccess(private val apiKey: String) {
    private val client = OkHttpClient()

    fun getWebResourceInfo(query: String): WebResourceResponse? {
        val responseStr = callApi("https://api.byu.edu:443/accessManagement/v1/webResource/pattern/${query}")

        return if (responseStr != null)
            decoder.decodeFromString(WebResourceResponse.serializer(), responseStr)
        else
            null
    }

    fun getWebResourcePolicies(webResId: String): ResourcePolicyResponse? {
        val responseStr = callApi("https://api.byu.edu:443/accessManagement/v1/webResource/${webResId}/policies")

        return if (responseStr != null)
            decoder.decodeFromString(ResourcePolicyResponse.serializer(), responseStr)
        else
            null
    }

    private fun callApi(url: String): String? {
        val request = Request.Builder()
            .get()
            .url(url)
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .build()
        val response = client.newCall(request).execute()

        val body = response.body?.string()
        response.close()

        return body
    }
}